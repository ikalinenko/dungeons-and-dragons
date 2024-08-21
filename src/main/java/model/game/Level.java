package main.java.model.game;

import main.java.control.initializers.LevelInitializer;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.callbacks.MessageCallBack;
import main.java.view.InputReader;

public class Level {
    protected Board board;
    protected InputReader inputReader;
    protected MessageCallBack cb;
    protected LevelInitializer levelInitializer;

    public Level(Board board, InputReader inputReader, MessageCallBack cb, LevelInitializer levelInitializer) {
        this.inputReader = inputReader;
        this.cb = cb;
        this.levelInitializer = levelInitializer;
        this.board = board;
    }

    protected void playerTick() {
        // Handle player action based on input (move, attack, cast ability)
        String action = inputReader.nextAction();
        board.getPlayer().performAction(action, board);
        board.getPlayer().onTurn();
        board.removeDeadEnemies();
    }

    protected void enemiesTick() {
        for (Enemy enemy : board.getEnemies()) {
            enemy.onEnemyTurn(board.getPlayer(), board);
        }
        board.updateTrapVisibility();
    }

    public void run() {
        Player player = board.getPlayer();

        //cb.send("Your position is " + player.getPosition());

        while (!isLevelFinished()) {

            cb.send(board.toString());
            cb.send(board.getPlayer().description());

            playerTick();
            enemiesTick();

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
