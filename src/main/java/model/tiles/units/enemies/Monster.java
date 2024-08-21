package main.java.model.tiles.units.enemies;

import main.java.model.tiles.units.HeroicUnit;
import main.java.utils.Position;
import main.java.utils.generators.Generator;
import main.java.utils.generators.RandomGenerator;
import main.java.model.game.Board;
import main.java.model.tiles.units.players.Player;

public class Monster extends Enemy implements HeroicUnit {
    protected int visionRange;
    private final Generator generator;

    public Monster(char tile, String name, int hitPoints,
                   int attack, int defense, int visionRange, int experience) {
        super(tile, name, hitPoints, attack, defense, experience);
        this.visionRange = visionRange;
        this.generator = new RandomGenerator(); // Default random generator
    }

    @Override
    public void onEnemyTurn(Player player, Board board) {
        if (isInVisionRange(player)) {
            chasePlayer(player, board);
        } else {
            randomMove(board);
        }
    }

    protected boolean isInVisionRange(Player player) {
        return position.Range(player.getPosition()) < visionRange;
    }

    @Override
    public void castAbility(Board board) {
        Player targetPlayer = board.getPlayer();

        if (isInVisionRange(targetPlayer)) {

            int attackRoll = attack();
            int defenseRoll = targetPlayer.defend();
            int damageTaken = attackRoll - defenseRoll;
            targetPlayer.getHealth().takeDamage(damageTaken);

            cb.send(name + " attacks " + targetPlayer.getName() + " for " + attackRoll + " damage.");
            cb.send(targetPlayer.getName() + " rolled " + defenseRoll + " defence points.");
            cb.send(name + " hit " + targetPlayer.getName() + " for " + damageTaken + " damage.");

            if (!targetPlayer.alive()) {
                cb.send(targetPlayer.getName() + " was killed by " + name + ".");
                targetPlayer.onDeath();
            }
        }
    }

    protected void chasePlayer(Player player, Board board) {
        Position newPosition;
        Position playerPos = player.getPosition();

        int dx = position.getX() - playerPos.getX();
        int dy = position.getY() - playerPos.getY();

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                newPosition = moveLeft(board);
            } else {
                newPosition = moveRight(board);
            }
        } else {
            if (dy > 0) {
                newPosition = moveUp(board);
            } else {
                newPosition = moveDown(board);
            }
        }

        if (newPosition.equals(playerPos)) {
            visit(player);
        }
    }

    protected void randomMove(Board board) {
        int move = generator.generate(5);

        switch (move) {
            case 0 -> moveLeft(board);
            case 1 -> moveRight(board);
            case 2 -> moveUp(board);
            case 3 -> moveDown(board);
            default -> {
            }
            // Stay
        }
    }

    @Override
    public String description() {
        return super.description() +
                ", Vision Range: " + visionRange;
    }

    @Override
    public void updateVisibility(Board board) {
        //Do nothing
    }
}
