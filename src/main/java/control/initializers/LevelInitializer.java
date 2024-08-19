package control.initializers;

import model.game.Board;
import model.tiles.Empty;
import model.tiles.Tile;
import model.tiles.Wall;
import model.tiles.units.enemies.Enemy;
import model.tiles.units.players.Player;
import utils.Position;
import utils.callbacks.DeathCallBack;
import utils.callbacks.MessageCallBack;
import utils.generators.FixedGenerator;
import utils.generators.Generator;

import java.io.IOException;
import java.nio.file.Files;
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
    private MessageCallBack messageCallback;
    private DeathCallBack deathCallback;


    public LevelInitializer(int playerID) {
        TileFactory tileFactory = new TileFactory();
        this.tileFactory = tileFactory;
        this.playerID = playerID;
        this.player = tileFactory.producePlayer(playerID); // Ensure player is created
        this.generator = new FixedGenerator(); // Default generator
        this.messageCallback = System.out::println; // Default callback
        this.deathCallback = () -> System.out.println("Player has died."); // Default callback
    }

    public LevelInitializer(TileFactory tileFactory, Generator generator, MessageCallBack messageCallback, DeathCallBack deathCallback) {
        this.tileFactory = tileFactory;
        this.generator = generator;
        this.messageCallback = messageCallback;
        this.deathCallback = deathCallback;
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
                Position position = new Position(row, col);
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
                        player.initialize(position, generator, messageCallback, deathCallback);
                        tile = player;
                        break;
                    default:
                        if (TileFactory.enemyTypes.containsKey(c)) {
                            Enemy enemy = tileFactory.produceEnemy(c, position, generator, messageCallback, deathCallback);
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

    public void loadNextLevel(String nextLevelPath) {
        initLevel(nextLevelPath);
    }

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

    protected int getTotalLevels() {
        // Return the total number of levels available in the game
        return 4; // Example value, replace with actual logic
    }

}
