package main.java.model.tiles.units.players;

import main.java.model.tiles.units.HeroicUnit;
import main.java.model.game.Board;
import main.java.model.tiles.units.Unit;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;

import java.util.List;

public class Warrior extends Player implements HeroicUnit {
    protected int abilityCooldown;
    protected int remainingCooldown;

    protected static final int INIT_COOLDOWN = 0;
    protected static final int HEALTH_GAIN = 5;
    protected static final int ATTACK_GAIN = 2;
    protected static final int DEFENSE_GAIN = 1;

    //private List<Enemy> enemies;

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
        //cb.send(name + " cooldown has been reset.");

        health.increaseMax(healthGain());
        health.restore();
        attack += attackGain();
        defense += defenseGain();

        String warriorMessage = name + " reached level " + level + ": +"
                + healthGain() + " Health, +"
                + attackGain() + " Attack, +"
                + defenseGain() + " Defense.";

        cb.send(warriorMessage);
    }

    @Override
    public void onTurn() {
        if (abilityUsedThisTurn) {
            return;
        }
        if (remainingCooldown >= 1) {
            remainingCooldown--;
        }
    }

    @Override
    public void castAbility(Board board) {
        if (remainingCooldown > 0) {
            cb.send(name + " tried to cast Avenger's Shield but it's on cooldown for " + remainingCooldown + " more turns.");
            abilityUsedThisTurn = false;
            return;
        }

        remainingCooldown = abilityCooldown;

        // Get enemies from the board
        List<Enemy> enemiesInRange = board.getEnemies().stream()
                .filter(e -> e.getPosition().Range(position) < 3)
                .toList();

        int damage = health.getCapacity() / 10;

        // Apply healing
        int healAmount = 10 * defense;
        int newHealth = Math.min(health.getCurrent() + healAmount, health.getCapacity());
        health.heal(newHealth - health.getCurrent());
        cb.send(name + " casts Avenger's Shield, healing for " + healAmount + " health.");

        // Check if there are any enemies in range
        if (!enemiesInRange.isEmpty()) {
            // Randomly select a target
            Enemy target = enemiesInRange.get(generator.generate(enemiesInRange.size()));
            cb.send(name + " hits " + target.getName() + " for " + damage + " damage.");
            target.getHealth().takeDamage(damage);
            cb.send(target.getName() + " has now " + target.getHealth().getCurrent() + " health.");

            if (!target.alive()) {
                cb.send(target.getName() + " has been killed by " + name + "'s Avenger's Shield.");
                addExperience(target.experience());
                target.onDeath();
            }
        }

        cb.send(description());
    }

    @Override
    public String description() {
        return super.description() +
                ", Cooldown: " + remainingCooldown + "/" + abilityCooldown;
    }
}
