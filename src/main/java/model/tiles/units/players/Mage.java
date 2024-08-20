package main.java.model.tiles.units.players;

import main.java.model.tiles.units.HeroicUnit;
import main.java.utils.generators.RandomGenerator;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;
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

    public Mage(String name, int hitPoints, int attack, int defense,
                int manaPool, int manaCost, int spellPower, int hitCount, int abilityRange) {
        super(name, hitPoints, attack, defense);
        this.manaPool = manaPool;
        this.currentMana = manaPool / 4;
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

        String mageMessage = name + " reached level " + level + ": +"
                + healthGain() + " Health, +"
                + attackGain() + " Attack, +"
                + defenseGain() + " Defense, +"
                + manaGain() + " Maximum Mana, +"
                + spellPowerGain() + " Spell Power.";

        cb.send(mageMessage);
    }

    @Override
    public void onTurn() {
        if (abilityUsedThisTurn) {
            return;
        }
        currentMana = Math.min(manaPool, currentMana + level);
    }

    @Override
    public void castAbility(Board board) {
        if (currentMana < manaCost) {
            cb.send(name + " tried to cast Blizzard but doesn't have enough mana.");
            abilityUsedThisTurn = false;
            return;
        }

        // Get enemies from the board within range
        List<Enemy> enemiesInRange = board.getEnemies().stream()
                .filter(e -> e.getPosition().Range(position) < abilityRange)
                .toList();

        currentMana -= manaCost;

        int hits = 0;

        cb.send(name + " casts Blizzard.");

        while (hits < hitCount && !enemiesInRange.isEmpty()) {
            // Randomly select a target
            Enemy target = enemiesInRange.get(generator.generate(enemiesInRange.size()));

            int attackRoll = this.spellPower;
            int defenseRoll = target.defend();
            int damageTaken = attackRoll - defenseRoll;
            target.getHealth().takeDamage(damageTaken);

            cb.send(target.getName() + " rolled " + defenseRoll + " defense points.");
            cb.send(name + " hits " + target.getName() + " with Blizzard for " + damageTaken + " damage.");

            if (!target.alive()) {
                cb.send(target.getName() + " has been killed by " + name + "'s Blizzard.");
                addExperience(target.experience());
                target.onDeath();
            }

            hits++;
            // Re-filter the enemies list to ensure only living enemies are targeted
            enemiesInRange = enemiesInRange.stream()
                    .filter(Enemy::alive)
                    .toList();
        }

        //cb.send(description());
    }

    @Override
    public String description() {
        return super.description() +
                ", Mana: " + currentMana + "/" + manaPool +
                ", Mana cost: " + manaCost +
                ", Spell power: " + spellPower +
                ", Hit count: " + hitCount +
                ", Ability range: " + abilityRange;
    }
}
