package main.java.model.game;

import main.java.model.tiles.Empty;
import main.java.model.tiles.Tile;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Board {
    private Map<Position, Tile> board;
    private Tile tile;
    private List<Tile> tiles;
    private Player player;
    private List<Enemy> enemies;
    private final int width;

    public Board(List<Tile> tiles, Player p, List<Enemy> enemies, int width){
        this.player = p;
        this.enemies = enemies;
        this.width = width;
        this.board = new TreeMap<>();
        this.tiles = tiles;
        for(Tile t : tiles){
            this.tile = t;
            board.put(t.getPosition(), t);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(Map.Entry<Position, Tile> entry : board.entrySet()){
            //sb.append(entry.getKey());
            sb.append(entry.getValue().toString());
            if(entry.getKey().getX() == width - 1){
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public void updateTrapVisibility() {
        for (Enemy enemy : enemies) {
            enemy.updateVisibility(this);
        }
    }

    public void updateBoard() {
        // Handle updating the board after each turn, e.g., moving enemies, updating player position
    }

    public boolean allEnemiesDefeated() {
        return enemies.stream().noneMatch(Enemy::alive);
    }

    public void removeEnemy(Enemy enemy) {
        board.put(enemy.getPosition(), new Empty());
        enemies.remove(enemy);
    }

    public boolean isPositionFree(Position newPosition) {
        Tile tile = board.get(newPosition);
        //return tile != null && tile.isWalkable();
        return tile.isWalkable();
    }

    public void updateTile(Position position, Tile newTile) {
        // Set the position of the new tile
        newTile.setPosition(position);

        // Update the board map with the new tile at the given position
        board.put(position, newTile);
    }

    public Tile getTile(Position position) {
        int index = position.getY() * width + position.getX();
        return tiles.get(index);
    }

    /*
    public void removeEnemy(Enemy enemy) {
        Position enemyPos = enemy.getPosition();
        if (board.containsKey(enemyPos)) {
            board.put(enemyPos, new Empty());
            enemies.remove(enemy);
        }
    }
    */

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Map<Position, Tile> getBoard() {return board;}
}
