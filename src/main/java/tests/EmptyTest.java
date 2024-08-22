package main.java.tests;

import junit.framework.TestCase;
import main.java.model.tiles.Empty;
import main.java.model.tiles.units.Unit;
import main.java.utils.Position;

import static org.mockito.Mockito.*;

public class EmptyTest extends TestCase {

    private Empty emptyTile;
    private Unit mockUnit;

    public void setUp() {
        emptyTile = new Empty();
        emptyTile.initialize(new Position(0, 0));

        mockUnit = mock(Unit.class);
    }

    public void testInitialization() {
        assertEquals(new Position(0, 0), emptyTile.getPosition());
        assertEquals(',', emptyTile.getTile());
    }

    public void testAccept() {
        emptyTile.accept(mockUnit);

        verify(mockUnit).visit(emptyTile);  // Check that the unit interacts with an empty tile
    }

    public void testGetTileChar() {
        assertEquals(',', Empty.getEmptyTile());  // Check that the void tile symbol is correct
    }

    public void testIsWalkable() {
        assertTrue(emptyTile.isWalkable());  // Checking that the empty tile is walkable
    }

    public void testToString() {
        assertEquals(",", emptyTile.toString());  // Check the string representation of an empty tile
    }
}
