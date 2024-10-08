package main.java.model.tiles.units.players;

import main.java.model.tiles.units.HeroicUnit;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Enemy;

import java.util.List;

public class Hunter extends Player implements HeroicUnit {
    protected int range;
    protected int arrowsCount;
    protected int ticksCount;

    protected static final int ARROWS_GAIN = 10;
    protected static final int EXTRA_ATTACK_GAIN = 2;
    protected static final int EXTRA_DEFENSE_GAIN = 1;

    public Hunter(String name, int hitPoints, int attack, int defense, int range) {
        super(name, hitPoints, attack, defense);
        this.range = range;
        this.arrowsCount = ARROWS_GAIN * level;
        this.ticksCount = 0;
    }

    protected int arrowsGain() {
        return ARROWS_GAIN * level;
    }

    protected int attackGain() {
        return super.attackGain() + EXTRA_ATTACK_GAIN * level;
    }

    protected int defenseGain() {
        return super.defenseGain() + EXTRA_DEFENSE_GAIN * level;
    }

    @Override
    public void levelUp() {
        super.levelUp();

        this.arrowsCount += arrowsGain();

        String hunterMessage = name + " reached level " + level + ": +"
                + healthGain() + " Health, +"
                + attackGain() + " Attack, +"
                + defenseGain() + " Defense, +"
                + arrowsGain() + " Arrows.";

        cb.send(hunterMessage);
    }

    @Override
    public void onTurn() {
        if (ticksCount == ARROWS_GAIN) {
            arrowsCount += level;
            ticksCount = 0;
        } else {
            ticksCount++;
        }
    }

    @Override
    public void castAbility(Board board) {
        if (arrowsCount == 0) {
            cb.send(name + " cannot shoot. No arrows left.");
            return;
        }

        List<Enemy> enemiesInRange = board.getEnemies().stream()
                .filter(e -> e.getPosition().Range(position) < range)
                .toList();

        if (enemiesInRange.isEmpty()) {
            cb.send(name + " tried to shoot an arrow but no enemies were in range.");
            cb.send(description());
            return;
        }

        arrowsCount--;

        Enemy closestEnemy = findClosestEnemy(enemiesInRange);

        cb.send(name + " fired an arrow at " + closestEnemy.getName() + ".");

        int attackRoll = attack;
        int defenseRoll = closestEnemy.defend();
        int damageTaken = attackRoll - defenseRoll;
        closestEnemy.getHealth().takeDamage(damageTaken);

        cb.send(name + " attacks " + closestEnemy.getName() + " for " + attackRoll + " damage.");
        cb.send(closestEnemy.getName() + " rolled " + defenseRoll + " defense points.");
        cb.send(name + " hit " + closestEnemy.getName() + " for " + damageTaken + " damage.");

        if (!closestEnemy.alive()) {
            closestEnemy.onDeath();
            addExperience(closestEnemy.experience());
        }
    }

    public Enemy findClosestEnemy(List<Enemy> enemiesInRange) {
        // Find the minimum distance to any enemy
        double minDistance = enemiesInRange.stream()
                .mapToDouble(enemy -> position.Range(enemy.getPosition()))
                .min()
                .orElse(Double.MAX_VALUE);

        // Collect all enemies that are at the minimum distance
        List<Enemy> closestEnemies = enemiesInRange.stream()
                .filter(enemy -> position.Range(enemy.getPosition()) == minDistance)
                .toList();

        // Randomly select one if there are multiple closest enemies
        return closestEnemies.get(generator.generate(closestEnemies.size()));
    }

    @Override
    public String description() {
        return super.description() +
                ", Arrows: " + arrowsCount +
                ", Range: " + range +
                ", Ticks: " + ticksCount;
    }

    public int getArrowsCount() {
        return arrowsCount;
    }

    public int getTicksCount() {
        return ticksCount;
    }

    public int setArrowsCount(int arrowsCount) {return this.arrowsCount = arrowsCount;}
}
