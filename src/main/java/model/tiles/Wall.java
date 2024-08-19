package main.java.model.tiles;

import main.java.model.tiles.units.Unit;

public class Wall extends Tile {
    public static final char WALL_TILE = '#';

    public Wall() {
        super(WALL_TILE);
    }

    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }

    @Override
    public boolean isWalkable() {
        return false;
    }
}
