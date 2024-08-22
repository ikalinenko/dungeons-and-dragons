package main.java.tests;

import junit.framework.TestCase;
import main.java.model.tiles.Wall;
import main.java.model.tiles.units.Unit;
import main.java.utils.Position;

import static org.mockito.Mockito.*;

public class WallTest extends TestCase {

    private Wall wallTile;
    private Unit mockUnit;

    public void setUp() {
        wallTile = new Wall();
        wallTile.initialize(new Position(0, 0));
        mockUnit = mock(Unit.class);
    }

    public void testInitialization() {
        assertEquals(new Position(0, 0), wallTile.getPosition());
        assertEquals('#', wallTile.getTile());
    }

    public void testAccept() {
        wallTile.accept(mockUnit);

        verify(mockUnit).visit(wallTile);  // Checking that the unit interacts with the wall
    }

    public void testIsWalkable() {
        assertFalse(wallTile.isWalkable());  // We check that the wall is not walkable
    }

    public void testToString() {
        assertEquals("#", wallTile.toString());  // Checking the string representation of the wall
    }
}
