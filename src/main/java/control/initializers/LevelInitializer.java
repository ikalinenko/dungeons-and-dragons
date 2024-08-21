package main.java.control.initializers;

import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;
import main.java.utils.generators.RandomGenerator;
import main.java.model.game.Board;
import main.java.model.tiles.Empty;
import main.java.model.tiles.Tile;
import main.java.model.tiles.Wall;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.FixedGenerator;
import main.java.utils.generators.Generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LevelInitializer {
    private Board board;
    private int width;
    private int playerID;
    private List<Tile> tiles = new ArrayList<>();
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();

    private TileFactory tileFactory;
    private Generator generator;
    private MessageCallBack cb;
    private DeathCallBack dcb;


    public LevelInitializer(int playerID, Generator generator, MessageCallBack cb, DeathCallBack dcb) {
        TileFactory tileFactory = new TileFactory();
        this.tileFactory = tileFactory;
        this.playerID = playerID;
        this.player = tileFactory.producePlayer(playerID); // Ensure player is created
        this.generator = generator; // Default generator
        this.cb = cb; // Default callback
        this.dcb = dcb; // Default callback
    }

    public void initLevel(String levelPath) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(levelPath));
        } catch (IOException e) {
            throw new RuntimeException("Error reading level file: " + e.getMessage(), e);
        }

        // Calculate width using the static method
        int width = calculateWidth(levelPath);

        // Initialize the board map and tiles list
        Map<Position, Tile> board = new TreeMap<>();
        List<Tile> tiles = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();

        int row = 0;
        for (String line : lines) {
            int col = 0;
            for (char c : line.toCharArray()) {
                Position position = new Position(col, row);
                Tile tile = null;
                switch (c) {
                    case '.':
                        tile = tileFactory.produceEmpty(position);
                        break;
                    case '#':
                        tile = tileFactory.produceWall(position);
                        break;
                    case '@':
                        if (player == null) {
                            throw new IllegalStateException("Player has not been initialized.");
                        }
                        player.initialize(position, generator, cb, dcb);
                        tile = player;
                        break;
                    default:
                        if (TileFactory.enemyTypes.containsKey(c)) {
                            Enemy enemy = tileFactory.produceEnemy(c, position, generator, cb, dcb);
                            tile = enemy;
                            enemies.add(enemy);
                        } else {
                            throw new IllegalArgumentException("Unknown tile character: " + c);
                        }
                        break;
                }

                tiles.add(tile);
                board.put(position, tile);
                col++;
            }
            row++;
        }

        this.width = width; // Ensure width is correctly set
        this.board = new Board(tiles, player, enemies, width);
    }

    /*
    public void loadNextLevel(String nextLevelPath) {
        initLevel(nextLevelPath);
    }
     */

    public List<Tile> getTiles() {
        return tiles;
    }

    public Player getPlayer() {
        return player;
    }

    public Board getBoard() {return board;}

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public static int calculateWidth(String levelPath) {
        try {
            String firstLine = Files.readAllLines(Paths.get(levelPath)).get(0);
            return firstLine.length();
        } catch (IOException e) {
            throw new RuntimeException("Error calculating board width: " + e.getMessage());
        }
    }
}
