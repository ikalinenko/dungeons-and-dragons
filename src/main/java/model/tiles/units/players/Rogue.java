package main.java.model.tiles.units.players;

import main.java.model.tiles.units.HeroicUnit;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Enemy;

import java.util.List;

public class Rogue extends Player implements HeroicUnit {
    protected int cost;
    protected int currentEnergy;

    protected static final int MAX_ENERGY = 100;
    protected static final int EXTRA_ATTACK_GAIN = 3;

    public Rogue(String name, int hitPoints, int attack, int defense, int cost) {
        super(name, hitPoints, attack, defense);
        this.cost = cost;
        this.currentEnergy = MAX_ENERGY;
    }

    protected int attackGain() {
        return super.attackGain() + EXTRA_ATTACK_GAIN * level;
    }

    @Override
    public void levelUp() {
        super.levelUp();

        this.currentEnergy = MAX_ENERGY;

        String rogueMessage = name + " reached level " + level + ": +"
                + healthGain() + " Health, +"
                + attackGain() + " Attack, +"
                + defenseGain() + " Defense.";

        cb.send(rogueMessage);
    }

    @Override
    public void onTurn() {
        if (abilityUsedThisTurn) {
            return;
        }
        currentEnergy = Math.min(currentEnergy + 10, MAX_ENERGY);
    }

    @Override
    public void castAbility(Board board) {
        if (currentEnergy < cost) {
            cb.send(name + " tried to cast Fan of Knives but doesn't have enough energy.");
            abilityUsedThisTurn = false;
            return;
        }

        List<Enemy> enemiesInRange = board.getEnemies().stream()
                .filter(e -> e.getPosition().Range(position) < 2)
                .toList();

        currentEnergy -= cost;

        cb.send(name + " casts Fan of Knives.");

        for (Enemy target : enemiesInRange) {

            int attackRoll = attack;
            int defenseRoll = target.defend();
            int damageTaken = attackRoll - defenseRoll;
            target.getHealth().takeDamage(damageTaken);

            cb.send(name + " attacks " + target.getName() + " for " + attackRoll + " damage.");
            cb.send(target.getName() + " rolled " + defenseRoll + " defense points.");
            cb.send(name + " hits " + target.getName() + " with Fan of Knives for " + damageTaken + " damage.");

            if (!target.alive()) {
                target.onDeath();
                addExperience(target.experience());
            }
        }
    }

    @Override
    public String description() {
        return super.description() +
                ", Energy: " + currentEnergy + "/" + MAX_ENERGY +
                ", Cost: " + cost;
    }
}
