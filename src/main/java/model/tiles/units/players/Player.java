package main.java.model.tiles.units.players;

import java.util.Optional;

import main.java.model.game.Board;
import main.java.model.tiles.units.Unit;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;
import main.java.view.ScannerInputReader;

public abstract class Player extends Unit {
    protected int level;
    protected int experience;

    protected ScannerInputReader inputReader;

    public static final char PLAYER_TILE = '@';
    protected static final int LEVEL_REQUIREMENT = 50;
    protected static final int HEALTH_GAIN = 10;
    protected static final int ATTACK_GAIN = 4;
    protected static final int DEFENSE_GAIN = 1;

    public Player(String name, int hitPoints, int attack, int defense) {
        super(PLAYER_TILE, name, hitPoints, attack, defense);
        this.level = 1;
        this.experience = 0;
    }

    public Player initialize(Position p, Generator gen, MessageCallBack cb, DeathCallBack dcb) {
        super.initialize(p, gen, cb, dcb);
        //System.out.println("Player initialized at position: " + position);
        return this;
    }

    public Player(Position position) {
        this.position = position;
    }

    public void addExperience(int experienceValue) {
        this.experience += experienceValue;
        while (experience >= levelRequirement()) {
            levelUp();
        }
    }

    protected int levelRequirement() {
        return LEVEL_REQUIREMENT * level;    }

    protected int healthGain() {
        return HEALTH_GAIN * level;
    }

    protected int attackGain() {
        return ATTACK_GAIN * level;
    }

    protected int defenseGain() {
        return DEFENSE_GAIN * level;
    }

    public void levelUp() {
        this.experience -= levelRequirement();
        this.level++;

        health.increaseMax(healthGain());
        health.restore();
        attack += attackGain();
        defense += defenseGain();

        cb.send(name + " leveled up to " + level);
        cb.send("HP Gain: " + healthGain());
        cb.send("AP Gain: " + attackGain());
        cb.send("DP Gain: " + defenseGain());
    }

    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }

    public void visit(Player p) {
        //Do nothing
    }

    public void visit(Enemy e) {
        battle(e);
        if(!e.alive()) {
            addExperience(e.experience());
            e.onDeath();
        }
    }

    public abstract void onTurn();

    public abstract void castAbility();

    public void performAction(String action, Board board) {
        Position newPosition = null;

        switch (action) {
            case "w":
                newPosition = moveUp(board);
                break;
            case "s":
                newPosition = moveDown(board);
                break;
            case "a":
                newPosition = moveLeft(board);
                break;
            case "d":
                newPosition = moveRight(board);
                break;
            case "e":
                castAbility(); // Cast special ability
                break;
            case "q":
                // Do nothing
                cb.send(getName() + " chose to wait.");
                break;
            default:
                cb.send("Invalid action. Please choose a valid action.");
                break;
        }

        Position finalNewPosition = newPosition;
        Optional<Enemy> enemyOnPosition = board.getEnemies().stream()
                .filter(enemy -> enemy.getPosition().equals(finalNewPosition))
                .findFirst();

        enemyOnPosition.ifPresent(this::visit);
    }

    @Override
    public void onDeath() {
        this.tile = 'X';
        cb.send(name + " has died.");
        dcb.onDeath();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
