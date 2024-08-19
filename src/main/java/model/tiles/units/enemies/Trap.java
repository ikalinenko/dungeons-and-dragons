package main.java.model.tiles.units.enemies;

import main.java.model.tiles.units.enemies.Enemy;
import main.java.utils.Position;
import main.java.model.game.Board;
import main.java.model.tiles.Empty;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;

public class Trap extends Enemy {
    protected int visTime;
    protected int invisTime;
    protected int ticks = 1;
    protected boolean visible = true;

    public Trap(char tile, String name, int hitPoints, int attack,
                int defense, int experience, int visTime, int invisTime)
    {
        super(tile, name, hitPoints, attack, defense, experience);
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
        if (ticks < visTime) {
            visible = true;
        } else if (ticks >= visTime && ticks < (visTime + invisTime)) {
            visible = false;
        } else if (ticks == (visTime + invisTime)) {
            visible = true;
            ticks = 0; // Reset the ticks to start a new cycle
        }

        ticks++;
    }

    /*
    private void updateState() {
        if (ticks < visTime) {
            visible = true;
        } else if (ticks < visTime + invisTime) {
            visible = false;
        } else if (ticks == visTime + invisTime) {
            ticks = -1; // Reset the ticks to start a new cycle
        }

        ticks++;
    }
     */

    @Override
    public void updateVisibility(Board board) {
        if (isVisible()) {
            // Update the board to display the trap
            board.getBoard().put(getPosition(), this);
        } else {
            // Update the board to display an empty tile
            board.getBoard().put(getPosition(), new Empty());
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

    public char getDisplayCharacter() {
        if (isVisible()) {
            return tile; // or any character representing the Trap
        } else {
            return Empty.getTile(); // or the character for an empty tile
        }
    }
}
