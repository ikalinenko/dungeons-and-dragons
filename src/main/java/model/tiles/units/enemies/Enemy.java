package main.java.model.tiles.units.enemies;

import main.java.model.game.Board;
import main.java.model.tiles.units.Unit;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;
import main.java.model.game.Board;
import main.java.model.tiles.units.Unit;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Health;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;

public abstract class Enemy extends Unit {
    protected int experience;

    public Enemy(char tile, String name, int hitPoints, int attack, int defense, int experience) {
        super(tile, name, hitPoints, attack, defense);
        this.experience = experience;
        this.health = new Health(hitPoints);
    }

    public Unit initialize(Position p, Generator gen, MessageCallBack cb, DeathCallBack dcb) {
        super.initialize(p, gen, cb, dcb);
        return this;
    }

    public int experience() {
        return experience;
    }

    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }

    public void visit(Player p) {
        this.battle(p);
        if(!p.alive()) {
            p.onDeath();
        }
    }

    public void visit(Enemy e) {
        //Do nothing
    }

    public abstract void onEnemyTurn(Player player, Board board);

    public void onDeath() {
        //this.tile = '@';
        cb.send(name + " has died.");
    }

    // Method to handle visibility logic
    public abstract void updateVisibility(Board board);
}
