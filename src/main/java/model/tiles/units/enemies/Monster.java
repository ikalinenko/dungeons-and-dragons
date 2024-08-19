package main.java.model.tiles.units.enemies;

import main.java.model.game.Board;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.utils.generators.RandomGenerator;
import main.java.utils.generators.Generator;

public class Monster extends Enemy {
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

    private boolean isInVisionRange(Player player) {
        return position.Range(player.getPosition()) < visionRange;
    }

    private void chasePlayer(Player player, Board board) {
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

    private void randomMove(Board board) {
        int move = generator.generate(5);

        switch (move) {
            case 0:
                moveLeft(board);
                break;
            case 1:
                moveRight(board);
                break;
            case 2:
                moveUp(board);
                break;
            case 3:
                moveDown(board);
                break;
            default:
                // Stay
                break;
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
