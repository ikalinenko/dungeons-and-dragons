package main.java.model.tiles.units.enemies;

import main.java.model.tiles.units.HeroicUnit;
import main.java.model.game.Board;
import main.java.model.tiles.units.HeroicUnit;
import main.java.model.tiles.units.players.Player;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;

public class Boss extends Monster implements HeroicUnit {
    protected int abilityFrequency;
    protected int combatTicks;
    protected final int INIT_COMBAT_TICKS = 0;

    public Boss(char tile, String name, int hitPoints, int attack, int defense, int visionRange, int experience, int abilityFrequency) {
        super(tile, name, hitPoints, attack, defense, visionRange, experience);
        this.abilityFrequency = abilityFrequency;
        this.combatTicks = INIT_COMBAT_TICKS;
    }

    @Override
    public void onEnemyTurn(Player player, Board board) {
        if (isInVisionRange(player)) {
            if (combatTicks == abilityFrequency) {
                combatTicks = INIT_COMBAT_TICKS;
                castAbility(board);
            } else {
                combatTicks++;
                chasePlayer(player, board);
            }
        } else {
            combatTicks = INIT_COMBAT_TICKS;
            randomMove(board);
        }
    }

    @Override
    public void castAbility(Board board) {
        Player targetPlayer = board.getPlayer();

        if (isInVisionRange(targetPlayer)) {
            int attackRoll = attack;
            int defenseRoll = targetPlayer.defend();
            int damageTaken = targetPlayer.getHealth().takeDamage(attackRoll - defenseRoll);

            cb.send(name + " attacks " + targetPlayer.getName() + " for " + attackRoll + " damage.");
            cb.send(targetPlayer.getName() + " rolled " + defenseRoll + " defence points.");
            cb.send(name + " hit " + targetPlayer.getName() + " for " + damageTaken + " damage.");

            if (!targetPlayer.alive()) {
                cb.send(targetPlayer.getName() + " was killed by " + name + ".");
                targetPlayer.onDeath();
            }
        }
    }

    @Override
    public String description() {
        return super.description() +
                ", Ability frequency: " + abilityFrequency +
                ", Combat ticks: " + combatTicks;
    }
}
