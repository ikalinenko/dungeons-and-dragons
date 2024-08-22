package main.java.model.game;

import main.java.control.initializers.GameInitializer;
import main.java.control.initializers.LevelInitializer;
import main.java.utils.callbacks.MessageCallBack;
import main.java.view.ScannerInputReader;
import main.java.model.tiles.units.players.Player;

public class Game {
    private Level level;
    private Board board;

    private ScannerInputReader inputReader;
    private GameInitializer gameInitializer;
    private LevelInitializer levelInitializer;
    private MessageCallBack cb;

    public Game(String levelsPath, LevelInitializer levelInit, ScannerInputReader inputReader, Board board, MessageCallBack cb) {
        this.gameInitializer = new GameInitializer(levelsPath);
        this.levelInitializer = levelInit;
        this.inputReader = inputReader;
        this.board = board;
        this.cb = cb;
        loadNextLevel();
    }

    public void setLevel(Level level) {
        this.level = level;
    }

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

    public boolean isGameOver() {
        Player player = board.getPlayer();
        return !player.alive() || allLevelsPassed();
    }

    public boolean allLevelsPassed() {
        return gameInitializer.getCurrentLevelIndex() > gameInitializer.getTotalLevels();
    }

    public void endGame() {
        Player player = board.getPlayer();
        if (!player.alive()) {
            cb.send("Game Over.");
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
