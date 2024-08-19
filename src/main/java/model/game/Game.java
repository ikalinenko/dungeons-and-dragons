package model.game;

import control.initializers.BoardLoader;
import control.initializers.LevelInitializer;
import model.tiles.Tile;
import model.tiles.units.enemies.Enemy;
import model.tiles.units.players.Player;
import utils.callbacks.DeathCallBack;
import utils.callbacks.MessageCallBack;
import view.ScannerInputReader;

import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;

public class Game {
    private model.game.Board board;
    private Level level;
    private int currentLevelIndex = 0;

    private ScannerInputReader inputReader;
    private LevelInitializer levelInitializer;
    private MessageCallBack cb;
    private DeathCallBack dcb;

    public Game(model.game.Board board, LevelInitializer levelInit, ScannerInputReader inputReader, MessageCallBack cb, DeathCallBack dcb) {
        this.board = board;
        this.levelInitializer = levelInit;
        this.inputReader = inputReader;
        this.cb = cb;
        this.dcb = dcb;
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
            //level = levels[i];
            level.run();
            if (level.isLevelFinished()) {
                loadNextLevel();
            }
        }
        endGame();
    }


    protected void playerAction() {
        // Handle player action based on input (move, attack, cast ability)
        String action = inputReader.nextAction();
        board.getPlayer().performAction(action, board);
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

    private void loadNextLevel() {
        currentLevelIndex++;
        if (currentLevelIndex <= getTotalLevels()) {
            String nextLevelPath = getLevelPath(currentLevelIndex);
            levelInitializer.loadNextLevel(nextLevelPath);
            this.board = levelInitializer.getBoard();
            this.level = new Level(board, inputReader, cb, levelInitializer); // Update level reference with the new board
        }
    }

    public boolean isGameOver() {
        return !board.getPlayer().alive() || (currentLevelIndex > getTotalLevels());
    }

    private void displayBoardState() {
        cb.send(board.toString());
        cb.send(board.getPlayer().description());
    }

    private void endGame() {
        Player player = board.getPlayer();
        if (!player.alive()) {
            player.onDeath();
        } else {
            cb.send("Congratulations! You've cleared all levels.");
        }
    }

    public model.game.Board getBoard() {
        return board;
    }

    public void setBoard(model.game.Board board) {
        this.board = board;
    }

    private int getTotalLevels() {
        // Return the total number of levels available in the game
        return 4; // Example value, replace with actual logic
    }

    private String getLevelPath(int levelIndex) {
        // Return the path to the level file based on the level index
        return "C:\\Users\\AM\\IdeaProjects\\Assignment3_OOP\\out\\artifacts\\Assignment3_OOP_jar\\levels_dir\\level" + levelIndex + ".txt"; // Example path format
    }
}
