package main.java.control.initializers;

import main.java.model.tiles.Tile;
import main.java.utils.Position;
import main.java.model.game.Board;
import main.java.model.tiles.Tile;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BoardLoader {
    /*
    private List<Tile> tiles;
    private Player player;
    private List<Enemy> enemies;
    private int width;

    public BoardLoader(List<Tile> tiles, Player player, List<Enemy> enemies) {
        this.tiles = tiles;
        this.player = player;
        this.enemies = enemies;
        //this.width = calculateWidth(levelPath);
    }

    public Board loadBoard() {
        if (tiles == null || player == null || enemies == null) {
            throw new IllegalArgumentException("One of the parameters is null.");
        }

        Map<Position, Tile> tileMap = new TreeMap<>();
        for (Tile tile : tiles) {
            if (tile == null) {
                throw new IllegalArgumentException("Tile is null.");
            }
            tileMap.put(tile.getPosition(), tile);
        }

        return new Board(tiles, player, enemies, width);
    }
     */
}
