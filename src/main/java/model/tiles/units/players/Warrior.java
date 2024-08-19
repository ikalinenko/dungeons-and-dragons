package main.java.model.tiles.units.players;

import main.java.model.tiles.units.HeroicUnit;
import main.java.model.tiles.units.enemies.Enemy;

import java.util.List;

public class Warrior extends Player implements HeroicUnit {
    protected int abilityCooldown;
    protected int remainingCooldown;

    protected static final int INIT_COOLDOWN = 0;
    protected static final int HEALTH_GAIN = 5;
    protected static final int ATTACK_GAIN = 2;
    protected static final int DEFENSE_GAIN = 1;

    private List<Enemy> enemies;

    public Warrior(String name, int hitPoints, int attack, int defense, int abilityCooldown) {
        super(name, hitPoints, attack, defense);
        this.abilityCooldown = abilityCooldown;
        this.remainingCooldown = INIT_COOLDOWN;
    }

    @Override
    protected int healthGain() {
        return super.healthGain() + HEALTH_GAIN * level;
    }

    @Override
    protected int attackGain() {
        return super.attackGain() + ATTACK_GAIN * level;
    }

    @Override
    protected int defenseGain() {
        return super.defenseGain() + DEFENSE_GAIN * level;
    }

    @Override
    public void levelUp() {
        super.levelUp();

        this.remainingCooldown = INIT_COOLDOWN;
        cb.send(name + " cooldown has been reset.");

        health.increaseMax(healthGain());
        health.restore();
        attack += attackGain();
        defense += defenseGain();

        cb.send(name + "HP Gain: " + healthGain());
        cb.send(name + "AP Gain: " + attackGain());
        cb.send(name + "DP Gain: " + defenseGain());
    }

    public void onTurn() {
        if (remainingCooldown > 0)
            remainingCooldown--;
    }

    public void castAbility() {
        if (remainingCooldown > 0) {
            cb.send(name + " tried to cast Avenger's Shield but it's on cooldown for " + remainingCooldown + " more turns.");
            return;
        }

        List<Enemy> enemiesInRange = enemies.stream()
                .filter(e -> e.getPosition().Range(position) < 3)
                .toList();

        if (enemiesInRange.isEmpty()) {
            cb.send(name + " tried to cast Avenger's Shield but there are no enemies in range.");
            return;
        }

        remainingCooldown = abilityCooldown;

        int damage = health.getCapacity() / 10;

        int healAmount = 10 * defense;
        int newHealth = Math.min(health.getCurrent() + healAmount, health.getCapacity());
        health.heal(newHealth - health.getCurrent()); // Heal by the amount needed to reach newHealth
        cb.send(name + " casts Avenger's Shield, healing for " + healAmount + " health.");

        Enemy target = enemiesInRange.get(generator.generate(enemiesInRange.size()));
        cb.send(name + " hits " + target.getName() + " for " + damage + " damage.");
        target.getHealth().takeDamage(damage);

        if (!target.alive()) {
            cb.send(target.getName() + " has been killed by " + name + "'s Avenger's Shield.");
            addExperience(target.experience());
            target.onDeath();
        }
    }

    @Override
    public String description() {
        return super.description() +
                " Ability cooldown: " + abilityCooldown +
                ", Remaining cooldown: " + remainingCooldown;
    }
}
