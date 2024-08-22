package main.java.tests;

import junit.framework.TestCase;
import main.java.model.tiles.Tile;
import main.java.model.tiles.units.Unit;
import main.java.utils.Position;

public class TileTest extends TestCase {

    private Tile tile;

    public void setUp() {
        tile = new TestTile('T');
        tile.initialize(new Position(0, 0));
    }

    public void testInitialization() {
        assertEquals(new Position(0, 0), tile.getPosition());
        assertEquals('T', tile.getTile());
    }

    public void testSwapPosition() {
        Tile anotherTile = new TestTile('A');
        anotherTile.initialize(new Position(1, 1));

        tile.swapPosition(anotherTile);

        assertEquals(new Position(1, 1), tile.getPosition());
        assertEquals(new Position(0, 0), anotherTile.getPosition());
    }

    public void testToString() {
        assertEquals("T", tile.toString());
    }

    public void testTileAt() {
        Position newPosition = new Position(2, 2);
        char returnedTile = tile.tileAt(newPosition);

        assertEquals('T', returnedTile);
        assertEquals(newPosition, tile.getPosition());
    }

    public void testSetPosition() {
        Position newPosition = new Position(3, 3);
        tile.setPosition(newPosition);

        assertEquals(newPosition, tile.getPosition());
    }

    public void testIsWalkable() {
        assertFalse(tile.isWalkable());  // Temporary class returns false
    }

    // A temporary subclass for testing abstract Tile
    private static class TestTile extends Tile {

        public TestTile(char tile) {
            super(tile);
        }

        @Override
        public void accept(Unit unit) {
            // Implementation for test
        }

        @Override
        public boolean isWalkable() {
            return false;  // For testing purposes, we return false
        }
    }
}
