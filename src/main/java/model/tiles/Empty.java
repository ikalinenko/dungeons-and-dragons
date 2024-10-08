package main.java.model.tiles;

import main.java.model.tiles.units.Unit;

public class Empty extends Tile {
    private static final char EMPTY_TILE = ',';

    public Empty() {
        super(EMPTY_TILE);
    }

    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }

    public static char getEmptyTile() {
        return EMPTY_TILE;
    }

    @Override
    public boolean isWalkable() {
        return true;
    }
}
