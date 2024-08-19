package main.java.model.tiles.units.players;

import main.java.model.tiles.units.HeroicUnit;
import main.java.model.tiles.units.players.Player;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;

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

        // Get enemies from the board within range
        List<Enemy> enemiesInRange = board.getEnemies().stream()
                .filter(e -> e.getPosition().Range(position) < range)
                .toList();

        if (enemiesInRange.isEmpty()) {
            cb.send(name + " tried to shoot an arrow but no enemies were in range.");
            cb.send(description());
            return;
        }

        arrowsCount--;

        // Find the closest enemy or pick one randomly if there are multiple at the same distance
        Enemy closestEnemy = enemiesInRange.get(generator.generate(enemiesInRange.size()));

        int damageDealt = attack - closestEnemy.defend(); // Calculate damage after defense
        if (damageDealt > 0) {
            cb.send(name + " shoots an arrow at " + closestEnemy.getName() + " for " + damageDealt + " damage!");
            closestEnemy.getHealth().takeDamage(damageDealt);

            if (!closestEnemy.alive()) {
                cb.send(closestEnemy.getName() + " has been killed by " + name + "'s arrow.");
                addExperience(closestEnemy.experience());
                closestEnemy.onDeath();
            }
        } else {
            cb.send(closestEnemy.getName() + " successfully defends against " + name + "'s attack.");
        }

        cb.send(description());
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
                ", Arrows: " + arrowsCount +
                ", Range: " + range +
                ", Ticks: " + ticksCount;
    }
}
