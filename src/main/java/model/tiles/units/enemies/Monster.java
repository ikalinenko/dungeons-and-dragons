package model.tiles.units.enemies;

import model.game.Board;
import model.tiles.units.players.Player;
import utils.Position;
import utils.callbacks.DeathCallBack;
import utils.callbacks.MessageCallBack;
import utils.generators.RandomGenerator;
import utils.generators.Generator;

public class Monster extends model.tiles.units.enemies.Enemy {
    protected int visionRange;
    //private Generator generator;

    public Monster(char tile, String name, int hitPoints,
                   int attack, int defense, int visionRange, int experience) {
        super(tile, name, hitPoints, attack, defense, experience);
        this.visionRange = visionRange;
        //this.generator = new RandomGenerator(); // Default random generator
    }

    @Override
    public void onEnemyTurn(Player player, Board board) {
        if (isInVisionRange(player)) {
            chasePlayer(player);
        } else {
            randomMove();
        }
    }

    private boolean isInVisionRange(Player player) {
        return position.Range(player.getPosition()) < visionRange;
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
                ", Vision Range: " + visionRange;
    }

    @Override
    public void updateVisibility(Board board) {
        //Do nothing
    }
}
