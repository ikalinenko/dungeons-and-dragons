package main.java.model.tiles.units.enemies;

import main.java.utils.Position;
import main.java.model.game.Board;
import main.java.model.tiles.Empty;
import main.java.model.tiles.units.players.Player;

public class Trap extends Enemy {
    protected final char trapTile;
    protected int visTime;
    protected int invisTime;
    protected int ticks = 0;
    protected boolean visible = true;

    public Trap(char trapTile, String name, int hitPoints, int attack,
                int defense, int experience, int visTime, int invisTime)
    {
        super(trapTile, name, hitPoints, attack, defense, experience);
        this.trapTile = trapTile;
        this.visTime = visTime;
        this.invisTime = invisTime;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void onEnemyTurn(Player player, Board board) {
        updateState(); // Update the trap's state

        if (isInRange(player)) {
            visit(player); // Perform actions if player is in range
        }
    }

    private void updateState() {
        ticks++;
        if (ticks < visTime) {
            visible = true;
        } else if (ticks >= visTime && ticks < visTime + invisTime) {
            visible = false;
        } else if (ticks == visTime + invisTime) {
            visible = true;
            ticks = 0;
        }
    }

    @Override
    public void updateVisibility(Board board) {
        if (isVisible()) {
            this.tile = trapTile;
        } else {
            this.tile = Empty.getTile();
        }
    }

    private boolean isInRange(Player player) {
        return position.Range(player.getPosition()) < 2;
    }

    /*
    private void attack(Player player) {
        player.getHealth().takeDamage(this.attack);
        cb.send(getName() + " attacked " + player.getName() + " for " + this.attack + " damage.");
    }
     */

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
