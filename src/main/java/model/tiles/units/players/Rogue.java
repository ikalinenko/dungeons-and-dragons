package main.java.model.tiles.units.players;

import main.java.model.tiles.units.HeroicUnit;
import main.java.model.tiles.units.enemies.Enemy;

import java.util.List;

public class Rogue extends Player implements HeroicUnit {
    protected int cost;
    protected int currentEnergy;

    protected static final int MAX_ENERGY = 100;
    protected static final int ATTACK_GAIN = 3;

    public Rogue(String name, int hitPoints, int attack, int defense, int cost) {
        super(name, hitPoints, attack, defense);
        this.cost = cost;
        this.currentEnergy = MAX_ENERGY;
    }

    protected int attackGain() {
        return super.attackGain() + ATTACK_GAIN * level;
    }

    @Override
    public void levelUp() {
        super.levelUp();
        this.currentEnergy = MAX_ENERGY;
        this.attack += attackGain();
    }

    public void onTurn() {
        currentEnergy = Math.min(currentEnergy + 10, MAX_ENERGY);
    }

    public void castAbility() {
        if (currentEnergy < cost) {
            cb.send(name + " tried to cast Fan of Knives but doesn't have enough energy.");
            return;
        }

        currentEnergy -= cost;

        List<Enemy> enemiesInRange = getEnemies().stream()
                .filter(e -> e.getPosition().Range(position) < 2)
                .toList();

        for (Enemy target : enemiesInRange) {
            int damageDealt = attack - target.defend(); // Calculate the damage after defense
            if (damageDealt > 0) {
                cb.send(name + " hits " + target.getName() + " with Fan of Knives for " + damageDealt + " damage.");
                target.getHealth().takeDamage(damageDealt);

                if (!target.alive()) {
                    cb.send(target.getName() + " has been killed by " + name + "'s Fan of Knives.");
                    addExperience(target.experience());
                    target.onDeath();
                }
            } else {
                cb.send(target.getName() + " successfully defends against " + name + "'s Fan of Knives.");
            }
        }
    }

    private List<Enemy> getEnemies() {
        return List.of();
    }

    @Override
    public String description() {
        return super.description() +
                " Cost: " + cost +
                ", Current energy: " + currentEnergy;
    }
}
