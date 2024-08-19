package model.tiles.units.enemies;

import model.game.Board;
import model.tiles.units.HeroicUnit;
import model.tiles.units.players.Player;
import utils.Position;
import utils.callbacks.DeathCallBack;
import utils.callbacks.MessageCallBack;
import utils.generators.Generator;

public class Boss extends Enemy implements HeroicUnit {
    protected int visionRange;
    protected int abilityFrequency;
    protected int combatTicks;
    protected final int INIT_COMBAT_TICKS = 0;

    private Player targetPlayer;

    public Boss(char tile, String name, int hitPoints, int attack,
                int defense, int visionRange, int experience, int abilityFrequency) {
        super(tile, name, hitPoints, attack, defense, experience);
        this.visionRange = visionRange;
        this.abilityFrequency = abilityFrequency;
        this.combatTicks = INIT_COMBAT_TICKS;
    }

    public void onEnemyTurn(Player player, Board board) {
        if (isInVisionRange(player)) {
            if (combatTicks == abilityFrequency) {
                combatTicks = INIT_COMBAT_TICKS;
                castAbility(board); // Pass the board to castAbility
            } else {
                combatTicks++;
                chasePlayer(player);
            }
        } else {
            combatTicks = INIT_COMBAT_TICKS;
            randomMove();
        }
    }

    @Override
    public void updateVisibility(Board board) {
        //Do nothing
    }

    private boolean isInVisionRange(Player player) {
        return position.Range(player.getPosition()) < visionRange;
    }

    @Override
    public void castAbility(Board board) {
        // Get the player from the board (assuming there is a method to retrieve the targeted player)
        Player targetPlayer = board.getPlayer();

        if (targetPlayer != null && isInVisionRange(targetPlayer)) {
            // Cast the ability by attacking the player
            int attackPoints = attack();
            int damageDealt = attackPoints - targetPlayer.defend();
            targetPlayer.getHealth().takeDamage(damageDealt);

            // Send a message about the attack
            cb.send(name + " attacks " + targetPlayer.getName() + " with " + attackPoints + " damage.");
        } else {
            cb.send(name + " cannot find a player within vision range to attack.");
        }
    }

    private void chasePlayer(Player player) {
        Position playerPos = player.getPosition();
        int dx = position.getX() - playerPos.getX();
        int dy = position.getY() - playerPos.getY();

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                moveLeft();
            } else {
                moveRight();
            }
        } else {
            if (dy > 0) {
                moveUp();
            } else {
                moveDown();
            }
        }
    }

    private void randomMove() {
        int move = generator.generate(5);

        switch (move) {
            case 0:
                moveLeft();
                break;
            case 1:
                moveRight();
                break;
            case 2:
                moveUp();
                break;
            case 3:
                moveDown();
                break;
            default:
                // Stay
                break;
        }
    }

    private void moveLeft() {
        position.setX(position.getX() - 1);
    }

    private void moveRight() {
        position.setX(position.getX() + 1);
    }

    private void moveUp() {
        position.setY(position.getY() - 1);
    }

    private void moveDown() {
        position.setY(position.getY() + 1);
    }

    @Override
    public String description() {
        return super.description() +
                " Vision range: " + visionRange +
                ", Ability frequency: " + abilityFrequency +
                ", Combat ticks: " + combatTicks;
    }
}
