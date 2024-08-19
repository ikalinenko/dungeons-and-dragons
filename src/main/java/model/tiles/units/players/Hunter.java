package main.java.model.tiles.units.players;

import main.java.model.tiles.units.HeroicUnit;
import main.java.model.tiles.units.enemies.Enemy;

import java.util.List;
import java.util.stream.Collectors;

public class Hunter extends Player implements HeroicUnit {
    protected int range;
    protected int arrowsCount;
    protected int ticksCount;

    protected static final int ARROWS_GAIN = 10;
    protected static final int ATTACK_GAIN = 2;
    protected static final int DEFENSE_GAIN = 1;

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
        return super.attackGain() + ATTACK_GAIN * level;
    }

    protected int defenseGain() {
        return super.defenseGain() + DEFENSE_GAIN * level;
    }

    @Override
    public void levelUp() {
        super.levelUp();
        this.arrowsCount += arrowsGain();
        this.attack += attackGain();
        this.defense += defenseGain();
    }

    public void onTurn() {
        if (ticksCount == ARROWS_GAIN) {
            arrowsCount += level;
            ticksCount = 0;
        } else {
            ticksCount++;
        }
    }

    @Override
    public void castAbility() {
        if (arrowsCount == 0 || !hasEnemyInRange()) {
            cb.send("Cannot cast ability. Either no arrows left or no enemies in range.");
            return;
        }

        arrowsCount--;
        Enemy closestEnemy = findClosestEnemy();

        if (closestEnemy != null) {
            int damageDealt = attack - closestEnemy.defend(); // Calculate damage after defense
            if (damageDealt > 0) {
                cb.send(name + " shoots an arrow at " + closestEnemy.getName() + " for " + damageDealt + " damage!");
                closestEnemy.getHealth().takeDamage(damageDealt);

                if (!closestEnemy.alive()) {
                    addExperience(closestEnemy.experience());
                    closestEnemy.onDeath();
                }
            } else {
                cb.send(closestEnemy.getName() + " successfully defends against " + name + "'s attack.");
            }
        }
    }

    private boolean hasEnemyInRange() {
        return findClosestEnemy() != null;
    }

    private Enemy findClosestEnemy() {
        List<Enemy> enemiesInRange = getEnemies().stream()
                .filter(e -> e.getPosition().Range(position) <= range)
                .collect(Collectors.toList());

        // Find the minimum range
        double minRange = enemiesInRange.stream()
                .mapToDouble(e -> e.getPosition().Range(position))
                .min()
                .orElse(Double.MAX_VALUE); // Handle the case where no enemies are in range

        // Filter enemies with the minimum range
        List<Enemy> closestEnemies = enemiesInRange.stream()
                .filter(e -> e.getPosition().Range(position) == minRange)
                .collect(Collectors.toList());

        // Randomly select one enemy from the closest enemies
        if (!closestEnemies.isEmpty()) {
            int randomIndex = generator.generate(closestEnemies.size());
            return closestEnemies.get(randomIndex);
        }

        return null; // No enemies in range
    }

    private List<Enemy> getEnemies() {
        return List.of();
    }

    @Override
    public String description() {
        return super.description() +
                " Arrows: " + arrowsCount +
                ", Range: " + range +
                ", Ticks: " + ticksCount;
    }
}
