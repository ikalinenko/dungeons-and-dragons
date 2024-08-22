package main.java.tests.units.players;

import junit.framework.TestCase;
import main.java.model.game.Board;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.utils.generators.RandomGenerator;

public class PlayerTest extends TestCase {

    private Player player;
    private Position startPosition;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        startPosition = new Position(0, 0);
        player = new TestPlayer("Hero", 100, 20, 10);
        player.initialize(startPosition, new RandomGenerator(), message -> {}, () -> {});
    }

    @Override
    public void tearDown() throws Exception {
        player = null;
        startPosition = null;
    }

    public void testInitialize() {
        assertEquals(1, player.getLevel());
        assertEquals(0, player.getExperience());
        assertEquals(startPosition, player.getPosition());
    }

    public void testAddExperience() {
        player.addExperience(60);
        assertEquals(1, player.getLevel());
        assertEquals(10, player.getExperience());

        player.addExperience(50);
        assertEquals(2, player.getLevel());
        assertEquals(10, player.getExperience());
    }

    public void testLevelRequirement() {
        assertEquals(50, player.getLevelRequirement());
        player.addExperience(100);
        assertEquals(100, player.getLevelRequirement());
    }

    public void testHealthGain() {
        assertEquals(10, player.getHealthGain());
        player.addExperience(50);
        assertEquals(20, player.getHealthGain());
    }

    public void testAttackGain() {
        assertEquals(4, player.getAttackGain());
        player.addExperience(50);
        assertEquals(8, player.getAttackGain());
    }

    public void testDefenseGain() {
        assertEquals(1, player.getDefenseGain());
        player.addExperience(50);
        assertEquals(2, player.getDefenseGain());
    }

    public void testLevelUp() {
//        int initialHealth = player.getHealth().getMax();
        int initialAttack = player.getAttack();
        int initialDefense = player.getDefense();

        player.addExperience(100);
        assertEquals(2, player.getLevel());
//        assertTrue(player.getHealth().getMax() > initialHealth);
        assertTrue(player.getAttack() > initialAttack);
        assertTrue(player.getDefense() > initialDefense);
    }

    public void testAccept() {
        // Test with a mock unit or an actual implementation
    }

    public void testVisit() {
        // Test visiting another player or an enemy
    }

    public void testOnTurn() {
        // Test onTurn logic
    }

    public void testSetPosition() {
        Position newPosition = new Position(2, 2);
        player.setPosition(newPosition);
        assertEquals(newPosition, player.getPosition());
    }

    public void testOnDeath() {
        player.onDeath();
        assertEquals('X', player.getTile());
    }

    private static class TestPlayer extends Player {
        public TestPlayer(String name, int hitPoints, int attack, int defense) {
            super(name, hitPoints, attack, defense);
        }

        @Override
        public void onTurn() {
            // Implementation for test
        }

        @Override
        public void castAbility(Board board) {
            // Implementation for test
        }
    }
    
    private static class TestRandomGenerator extends RandomGenerator {}
}
