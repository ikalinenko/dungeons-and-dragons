package main.java.model.tiles.units.players;

import main.java.model.tiles.units.HeroicUnit;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.utils.generators.RandomGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class Mage extends Player implements HeroicUnit {
    protected int currentMana;
    protected int manaPool;
    protected int manaCost;
    protected int spellPower;
    protected int hitCount;
    protected int abilityRange;

    protected final int INIT_MANA = manaPool / 4;
    protected static final int MANA_GAIN = 25;
    protected static final int SPELL_GAIN = 10;

    private List<Enemy> enemies;

    public Mage(String name, int hitPoints, int attack, int defense,
                int manaPool, int manaCost, int spellPower, int hitCount, int abilityRange) {
        super(name, hitPoints, attack, defense);
        this.manaPool = manaPool;
        this.currentMana = INIT_MANA;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitCount = hitCount;
        this.abilityRange = abilityRange;
        this.generator = new RandomGenerator();
    }

    protected int manaGain() {
        return MANA_GAIN * level;
    }

    protected int spellPowerGain() {
        return SPELL_GAIN * level;
    }

    @Override
    public void levelUp() {
        super.levelUp();
        this.manaPool += manaGain();
        this.currentMana = Math.min(this.currentMana + (INIT_MANA), manaPool);
        this.spellPower += spellPowerGain();
    }

    public void onTurn() {
        currentMana = Math.min(manaPool, currentMana + level);
    }

    public void castAbility() {
        if (currentMana < manaCost) {
            cb.send(name + " tried to cast Blizzard but doesn't have enough mana.");
            return;
        }

        currentMana -= manaCost;
        int hits = 0;

        List<Enemy> enemiesInRange = enemies.stream()
                .filter(e -> e.getPosition().Range(position) < abilityRange)
                .collect(Collectors.toList());

        while (hits < hitCount && !enemiesInRange.isEmpty()) {
            Enemy target = enemiesInRange.get(generator.generate(enemiesInRange.size()));
            cb.send(name + " hits " + target.getName() + " with Blizzard for " + spellPower + " damage.");
            target.getHealth().takeDamage(spellPower);

            if (!target.alive()) {
                cb.send(target.getName() + " has been killed by " + name + "'s Blizzard.");
                addExperience(target.experience());
                target.onDeath();
            }

            hits++;
            // Re-filter the enemies list in case some have died
            enemiesInRange = enemiesInRange.stream()
                    .filter(Enemy::alive)
                    .collect(Collectors.toList());
        }
    }

    /*
    private List<Enemy> getEnemies() {
        // Implement this method to retrieve the list of enemies
        return List.of(); // Placeholder
    }
    */

    @Override
    public String description() {
        return super.description() +
                " Current mana: " + currentMana +
                " Mana pool: " + manaPool +
                " Mana cost: " + manaCost +
                " Spell power: " + spellPower +
                " Hit count: " + hitCount +
                ", Ability range: " + abilityRange;
    }
}
