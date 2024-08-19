package main.java.control.initializers;

import main.java.model.tiles.Tile;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;
import main.java.utils.generators.RandomGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LevelInitializer {
    private int playerID;
    private List<Tile> tiles = new ArrayList<>();
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();

    public LevelInitializer(int playerID) {
        this.playerID = playerID;
    }

    public void initLevel(String levelPath) {
        TileFactory tileFactory = new TileFactory();

        // Initialize the callbacks and generator
        Generator generator = new RandomGenerator(); // or any other generator you prefer
        MessageCallBack messageCallback = System.out::println; // Example callback
        DeathCallBack deathCallback = () -> System.out.println("Enemy has died."); // Example callback

        // Initialize the player
        player = tileFactory.producePlayer(playerID);

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(levelPath));
        } catch (IOException e) {
            throw new RuntimeException("Error reading level file: " + e.getMessage(), e);
        }

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
                        player.initialize(position, generator, messageCallback, deathCallback);
                        tile = player;  // Add the player to the tiles list
                        break;
                    default:
                        if (TileFactory.enemyTypes.containsKey(c)) {
                            Enemy enemy = tileFactory.produceEnemy(c, position, generator, messageCallback, deathCallback);
                            tile = enemy;  // Add the enemy to the tiles list
                            enemies.add(enemy);  // Add the enemy to the enemies list
                        } else {
                            throw new IllegalArgumentException("Unknown tile character: " + c);
                        }
                        break;
                }

                /*
                // Debugging to check the tile and position
                if (tile == null) {
                    throw new IllegalStateException("Tile creation failed for character: " + c);
                }
                if (tile.getPosition() == null) {
                    throw new IllegalStateException("Tile position is null for character: " + c);
                }
                 */

                tiles.add(tile);
                col++;
            }
            row++;
        }

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
