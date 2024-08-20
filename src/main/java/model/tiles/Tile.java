package main.java.model.tiles;

import main.java.model.tiles.units.Unit;
import main.java.utils.Position;

public abstract class Tile {
    public char tile;
    protected Position position;

    public Tile(char tile) {
        this.tile = tile;
    }

    public Tile() {
    }

    public Tile initialize(Position p) {
        this.position = p;
        return this;
    }

    public void swapPosition(Tile t) {
        Position temp = t.position;
        t.position = this.position;
        this.position = temp;
    }

    @Override
    public String toString() {
        return String.valueOf(tile);
    }

    public abstract void accept(Unit unit);

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract boolean isWalkable();

    public char getTileChar() {
        return tile;
    }

    public char tileAt(Position position) {
        this.position = position;
        return tile;
    }
}
