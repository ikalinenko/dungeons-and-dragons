package main.java.model.game;

import main.java.control.initializers.BoardLoader;
import main.java.control.initializers.GameInitializer;
import main.java.control.initializers.LevelInitializer;
import main.java.control.initializers.LevelInitializer;
import main.java.model.game.Level;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.view.ScannerInputReader;
//import main.java.model.game.Board;
import main.java.model.tiles.Tile;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.view.ScannerInputReader;

import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class Game {
    private Level level;
    private Board board;

    private int currentLevelIndex = 0;

    private ScannerInputReader inputReader;
    private GameInitializer gameInitializer;
    private LevelInitializer levelInitializer;
    private MessageCallBack cb;
    private DeathCallBack dcb;

    public Game(Board board, LevelInitializer levelInit, ScannerInputReader inputReader, MessageCallBack cb, DeathCallBack dcb, String levelsPath) {
        this.board = board;
        this.levelInitializer = levelInit;
        this.inputReader = inputReader;
        this.cb = cb;
        this.dcb = dcb;
        this.gameInitializer = new GameInitializer(levelsPath);
    }

    /*
    public Game(Board board, MessageCallBack cb, DeathCallBack dcb) {
        this.board = board;
        this.cb = cb;
        this.dcb = dcb;
    }
    */

    public void setLevel(Level level) {
        this.level = level;
    }

    /*
    public void run() {
        Player player = board.getPlayer();

        if (player == null) {
            cb.send("No player found on the board.");
            return;
        }

        // Example player action
        player.onTurn();

        // For each enemy: enemy action
        for (Enemy enemy : board.getEnemies()) {
            // Example enemy action
            enemy.onEnemyTurn(player);
        }

        if (isGameOver()) {
            cb.send("Game Over!");
        }
    }
    */

    /*
    public void run() {
        while (!isGameOver()) {
            playerAction();
            for (Enemy enemy : board.getEnemies()) {
                enemy.onEnemyTurn(board.getPlayer());
            }
            board.updateBoard();
            displayBoardState();

            if (level.isLevelFinished()) {
                loadNextLevel();
            }
        }
        endGame();
    }
     */

    /*
    private void loadCurrentLevel() {
        // Obtain the current level's board data
        List<Tile> tiles = getTilesForCurrentLevel(); // Implement this method to fetch or create tiles
        Player player = getPlayerForCurrentLevel(); // Implement this method to fetch or create the player
        List<Enemy> enemies = getEnemiesForCurrentLevel(); // Implement this method to fetch or create enemies
        int width = getBoardWidthForCurrentLevel(); // Implement this method to fetch or set board width

        // Create a new Board instance with level-specific data
        Board newBoard = new Board(tiles, player, enemies, width);

        // Set the new board in the current level
        level.setBoard(newBoard);
    }
    */

    public void run() {
        while (!isGameOver()) {
            runCurrentLevel();

            if (level.isLevelFinished()) {
                loadNextLevel();
            }
        }

        endGame();
    }

    private void runCurrentLevel() {
        level.run();
    }

    private void loadNextLevel() {
        gameInitializer.loadNextLevel(levelInitializer);
        this.board = levelInitializer.getBoard();
        this.level = new Level(board, inputReader, cb, levelInitializer);
    }

    /*
    public boolean isGameOver() {
        Player player = board.getPlayer();
        // Check if the player is dead or if some level condition is met
        if (player != null && !player.alive()) {
            cb.send("Player has been defeated!");
            dcb.onDeath();
            return true;
        }

        // Additional conditions for game over based on levels or other criteria
        //level.index = len(level_list)
        return false;
    }
     */

    public boolean isGameOver() {
        Player player = board.getPlayer();
        return !player.alive() || allLevelsPassed();
    }

    public boolean allLevelsPassed() {
        return gameInitializer.getCurrentLevelIndex() > gameInitializer.getTotalLevels();
    }

    /*
    private void displayBoardState() {
        cb.send(board.toString());
        cb.send(board.getPlayer().description());
    }
     */

    private void endGame() {
        Player player = board.getPlayer();
        if (!player.alive()) {
            cb.send("Game Over.");
            //player.onDeath();
        } else {
            cb.send("Congratulations! You've cleared all levels.");
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

}
