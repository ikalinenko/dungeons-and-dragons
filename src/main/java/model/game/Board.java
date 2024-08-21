package main.java.model.game;

import main.java.model.tiles.Tile;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.model.tiles.Empty;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Board {
    private Map<Position, Tile> board;
    private Tile tile;
    private List<Tile> tiles;
    private Player player;
    private List<Enemy> enemies;
    private int width;

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

    public boolean allEnemiesDefeated() {
        return enemies.stream().noneMatch(Enemy::alive);
    }

    public void removeDeadEnemies() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (!enemy.alive()) {
                Position enemyPos = enemy.getPosition();

                // Debug: Print out the position before updating
                //System.out.println("Removing dead enemy at position: " + enemyPos);

                // Replace the enemy's position on the board with an Empty tile
                board.put(enemyPos, new Empty());

                // Debug: Verify that the tile was updated correctly
                Tile updatedTile = board.get(enemyPos);
                //System.out.println("Updated tile at position: " + updatedTile);

                iterator.remove(); // Safely remove the enemy from the list
            }
        }
    }

    /*
    public void removeEnemy(Enemy enemy) {
        board.put(enemy.getPosition(), new Empty());
        enemies.remove(enemy);
        }
     */

    public boolean isPositionFree(Position newPosition) {
        Tile tile = board.get(newPosition);
        return tile.isWalkable();
    }

    public void updateTile(Position position, Tile newTile) {
        // Set the position of the new tile
        newTile.setPosition(position);

        // Update the board map with the new tile at the given position
        board.put(position, newTile);
    }

    /*
    public Tile getTile(Position position) {
        return board.get(position);
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
