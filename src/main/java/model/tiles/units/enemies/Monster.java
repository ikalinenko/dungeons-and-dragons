package main.java.model.tiles.units.enemies;

import main.java.utils.Position;
import main.java.utils.generators.Generator;
import main.java.utils.generators.RandomGenerator;
import main.java.model.game.Board;
import main.java.model.tiles.units.players.Player;

public class Monster extends Enemy {
    protected int visionRange;
    private final Generator generator;

    public Monster(char tile, String name, int hitPoints,
                   int attack, int defense, int visionRange, int experience) {
        super(tile, name, hitPoints, attack, defense, experience);
        this.visionRange = visionRange;
        this.generator = new RandomGenerator();
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

    public void chasePlayer(Player player, Board board) {
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

    public void randomMove(Board board) {
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
