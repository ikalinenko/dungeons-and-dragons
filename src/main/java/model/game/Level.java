package model.game;

import control.initializers.LevelInitializer;
import model.tiles.units.players.Player;
import model.tiles.units.enemies.Enemy;
import utils.callbacks.MessageCallBack;
import view.InputReader;

public class Level {
    protected model.game.Board board;
    protected InputReader inputReader;
    protected MessageCallBack cb;
    protected LevelInitializer levelInitializer;

    //protected Player player;
    //protected List<Enemy> enemies = new ArrayList<>();

    public Level(model.game.Board board, InputReader inputReader, MessageCallBack cb, LevelInitializer levelInitializer) {
        this.inputReader = inputReader;
        this.cb = cb;
        this.levelInitializer = levelInitializer;
        this.board = board;
    }

    public void run() {
        Player player = board.getPlayer();
        cb.send(board.toString());
        cb.send(board.getPlayer().description());
        cb.send("Your position is " + player.getPosition());

        while (!isLevelFinished()) {
            String action = inputReader.nextAction();

            // Player's turn
            //player.onTurn();
            player.performAction(action, board);
            player.onTurn();

            board.removeDeadEnemies();

            // Enemies' turns
            for (Enemy enemy : board.getEnemies()) {
                enemy.onEnemyTurn(player, board);
            }

            board.removeDeadEnemies();
            board.updateTrapVisibility();

            // Print the board state after the turn
            cb.send(board.toString());
            cb.send(player.description());
            cb.send(board.getEnemies().toString());
        }
    }

    public boolean isLevelFinished() {
        return !board.getPlayer().alive() || board.allEnemiesDefeated();
    }

    public model.game.Board getBoard() {
        return board;
    }
}
