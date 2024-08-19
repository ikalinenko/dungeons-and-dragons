package model.tiles.units;

import model.tiles.Empty;
import model.tiles.Tile;
import model.tiles.Wall;
import model.tiles.units.enemies.Enemy;
import model.tiles.units.players.Player;
import utils.Health;
import utils.Position;
import utils.callbacks.DeathCallBack;
import utils.callbacks.MessageCallBack;
import utils.generators.Generator;

public abstract class Unit extends Tile {
    protected String name;
    protected Health health;
    protected int attack;
    protected int defense;

    protected Generator generator;
    protected MessageCallBack cb;
    protected DeathCallBack dcb;

    public Unit(char tile, String name, int hitPoints, int attack, int defense) {
        super(tile);
        this.name = name;
        this.health = new Health(hitPoints);
        this.attack = attack;
        this.defense = defense;
    }

    protected Unit() {
    }

    public Unit initialize(Position p, Generator generator, MessageCallBack cb, DeathCallBack dcb) {
        super.initialize(p);
        this.generator = generator;
        this.cb = cb;
        this.dcb = dcb;
        return this;
    }

    public int attack() {
        return generator.generate(attack);
    }

    public int defend(){
        return generator.generate(defense);
    }

    public boolean alive() {
        return health.getCurrent() > 0;
    }

    public void battle(Unit defender) {
        // Assuming this is the attacker
        int attackRoll = this.attack();
        int defenseRoll = defender.defend();
        int damage = attackRoll - defenseRoll;

        if (damage > 0) {
            int damageTaken = defender.health.takeDamage(damage);
            cb.send(this.description() + " dealt " + damage + " damage to " + defender.description());
        }

        if (!defender.alive()) {
            defender.onDeath();
        }
    }

    public void interact(Tile t) {
        t.accept(this);
    }

    public void visit(Empty e) {
        swapPosition(e);
    }

    public void visit(Wall w) {
        //Do Nothing
    }

    public abstract void accept(Unit unit);
    public abstract void visit(Player p);
    public abstract void visit(Enemy e);

    public void onDeath() {
        dcb.onDeath();
    }

    public String getName() {
        return name;
    }

    public Health getHealth() {
        return health;
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public String toString() {
        return Character.toString(tile);
    }

    public String description() {
        return name + " - Health: " + health.getCurrent() + "/" + health.getCapacity() + ", Attack: " + attack + ", Defense: " + defense;
    }
}
