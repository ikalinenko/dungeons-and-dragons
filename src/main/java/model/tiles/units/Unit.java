package main.java.model.tiles.units;

import main.java.model.tiles.Empty;
import main.java.model.tiles.Tile;
import main.java.model.tiles.Wall;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Health;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;
import main.java.model.tiles.Empty;
import main.java.model.tiles.Tile;
import main.java.model.tiles.Wall;
import main.java.model.game.Board;
import main.java.utils.Health;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;

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
        int damageTaken = defender.health.takeDamage(attack - defense);

        cb.send(this.name + " engaged in combat with " + defender.name + ".");
        cb.send(this.description());
        cb.send(defender.description());
        cb.send(this.name + " rolled " + attackRoll + " attack points.");
        cb.send(defender.name + " rolled " + defenseRoll + " defence points.");
        cb.send(this.name + " dealt " + damageTaken + " to " + defender.name + ".");

        /*
        if (damageTaken > 0) {
            cb.send(this.description() + " dealt " + damageTaken + " damage to " + defender.description());
        }
         */

        if (!defender.alive()) {
            defender.onDeath();
        }
    }

    public Position moveLeft(Board board) {
        Position newPosition = new Position(position.getX() - 1, position.getY());
        move(newPosition, board); // Move left

        return newPosition;
    }

    public Position moveRight(Board board) {
        Position newPosition = new Position(position.getX() + 1, position.getY());
        move(newPosition, board); // Move right

        return newPosition;
    }

    public Position moveUp(Board board) {
        Position newPosition = new Position(position.getX(), position.getY() + 1);
        move(newPosition, board); // Move up

        return newPosition;
    }

    public Position moveDown(Board board) {
        Position newPosition = new Position(position.getX(), position.getY() - 1);
        move(newPosition, board); // Move down

        return newPosition;
    }

    public boolean isValidMove(Position newPosition, Board board) {
        return board.isPositionFree(newPosition);
    }

    public void move(Position newPosition, Board board) {
        if (isValidMove(newPosition, board)) {
            // Replace the old position with an empty tile
            board.updateTile(position, new Empty());

            // Update player's position
            position = newPosition;

            // Place player at the new position
            board.updateTile(position, this);  // 'this' refers to the Unit (Tile)
            cb.send(getName() + " moved to " + newPosition);
        } else {
            cb.send(getName() + " cannot move to " + newPosition + ". It's blocked.");
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
