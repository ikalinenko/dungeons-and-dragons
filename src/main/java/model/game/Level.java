package main.java.model.game;

import main.java.control.initializers.LevelInitializer;
import main.java.control.initializers.LevelInitializer;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.callbacks.MessageCallBack;
import main.java.view.InputReader;
import main.java.model.game.Board;
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

    protected void playerAction() {
        // Handle player action based on input (move, attack, cast ability)
        String action = inputReader.nextAction();
        board.getPlayer().performAction(action, board);
        board.getPlayer().onTurn();
        board.removeDeadEnemies();
    }

    protected void enemiesAction() {
        for (Enemy enemy : board.getEnemies()) {
            enemy.onEnemyTurn(board.getPlayer(), board);
        }
        board.updateTrapVisibility();
    }

    public void run() {
        Player player = board.getPlayer();

        cb.send(board.toString());
        cb.send(player.description());
        //cb.send("Your position is " + player.getPosition());

        while (!isLevelFinished()) {
            /*
            // Player's turn
            String action = inputReader.nextAction();
            player.performAction(action, board);
            player.onTurn();
            board.removeDeadEnemies();
             */

            playerAction();

            /*
            // Enemies' turns
            for (Enemy enemy : board.getEnemies()) {
                enemy.onEnemyTurn(player, board);
            }
            board.removeDeadEnemies();
            board.updateTrapVisibility();
             */

            enemiesAction();

            // Print the board state after the turn
            cb.send(board.toString());
            cb.send(player.description());
            //cb.send(board.getEnemies().toString());
        }
    }

    public boolean isLevelFinished() {
        return !board.getPlayer().alive() || board.allEnemiesDefeated();
    }

    public Board getBoard() {
        return board;
    }
}
