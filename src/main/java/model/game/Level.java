package main.java.model.game;

import main.java.control.initializers.LevelInitializer;
import main.java.model.tiles.units.players.Player;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.utils.callbacks.MessageCallBack;
import main.java.view.InputReader;

public class Level {
    protected Board board;
    protected InputReader inputReader;
    protected MessageCallBack cb;
    protected LevelInitializer levelInitializer;

    //protected Player player;
    //protected List<Enemy> enemies = new ArrayList<>();

    public Level(Board board, InputReader inputReader, MessageCallBack cb, LevelInitializer levelInitializer) {
        this.inputReader = inputReader;
        this.cb = cb;
        this.levelInitializer = levelInitializer;
        this.board = board;
    }

    public void run() {
        Player player = board.getPlayer();
        cb.send(board.toString());
        cb.send("Your position is " + player.getPosition());

        while (!isLevelFinished()) {
            String action = inputReader.nextAction();

            // Player's turn
            player.performAction(action, board);

            //board.updateTrapVisibility();

            // Enemies' turns
            for (Enemy enemy : board.getEnemies()) {
                enemy.onEnemyTurn(player, board);
            }

            board.updateTrapVisibility();

            // Print the board state after the turn
            cb.send(board.toString());
            cb.send(player.description());
        }
    }

    public boolean isLevelFinished() {
        return !board.getPlayer().alive() || board.allEnemiesDefeated();
    }

    public Board getBoard() {
        return board;
    }
}
