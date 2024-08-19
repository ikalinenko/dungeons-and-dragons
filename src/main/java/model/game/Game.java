package main.java.model.game;

import main.java.control.initializers.LevelInitializer;
import main.java.model.tiles.units.players.Player;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.view.ScannerInputReader;

public class Game {
    private Board board;
    private Level level;
    private int currentLevelIndex = 0;

    private ScannerInputReader inputReader;
    private LevelInitializer levelInitializer;
    private MessageCallBack cb;
    private DeathCallBack dcb;

    public Game(Board board, ScannerInputReader inputReader, MessageCallBack cb, DeathCallBack dcb) {
        this.board = board;
        this.inputReader = inputReader;
        this.cb = cb;
        this.dcb = dcb;
    }

    public Game(Board board, MessageCallBack cb, DeathCallBack dcb) {
        this.board = board;
        this.cb = cb;
        this.dcb = dcb;
    }

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
        if (currentLevelIndex < getTotalLevels()) {
            String nextLevelPath = getLevelPath(currentLevelIndex);
            levelInitializer.loadNextLevel(nextLevelPath);
            this.board = level.getBoard();
            this.level = new Level(board, inputReader, cb, levelInitializer); // Update level reference with the new board
        }
    }

    private boolean isGameOver() {
        return !board.getPlayer().alive() || (currentLevelIndex >= getTotalLevels());
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

    public Board getBoard() {
        return board;
    }

    private int getTotalLevels() {
        // Return the total number of levels available in the game
        return 4; // Example value, replace with actual logic
    }

    private String getLevelPath(int levelIndex) {
        // Return the path to the level file based on the level index
        return "path/to/level" + levelIndex + ".txt"; // Example path format
    }
}
