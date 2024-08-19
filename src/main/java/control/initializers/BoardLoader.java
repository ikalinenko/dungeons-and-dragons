package control.initializers;

import model.game.Board;
import model.tiles.Tile;
import model.tiles.units.enemies.Enemy;
import model.tiles.units.players.Player;
import utils.Position;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BoardLoader {
    private List<Tile> tiles;
    private Player player;
    private List<Enemy> enemies;
    private int width;

    public BoardLoader(List<Tile> tiles, Player player, List<Enemy> enemies, String levelPath) {
        this.tiles = tiles;
        this.player = player;
        this.enemies = enemies;
        this.width = calculateWidth(levelPath);
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

    private int calculateWidth(String levelPath) {
        try {
            String firstLine = Files.readAllLines(Paths.get(levelPath)).get(0);
            return firstLine.length();
        } catch (IOException e) {
            throw new RuntimeException("Error calculating board width: " + e.getMessage());
        }
    }
}
