package main.java.tests.units.enemies;

import junit.framework.TestCase;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.utils.generators.Generator;

import static org.mockito.Mockito.*;

public class EnemyTest extends TestCase {

    private Enemy enemy;
    private Player mockPlayer;
    private Generator mockGenerator;

    public void setUp() {
        mockPlayer = mock(Player.class);
        mockGenerator = mock(Generator.class);

        enemy = new TestEnemy('E', "Goblin", 50, 10, 5, 20);
        enemy.initialize(new Position(0, 0), mockGenerator, message -> {}, () -> {});
    }

    public void testInitialization() {
        assertEquals("Goblin", enemy.getName());
        assertEquals(50, enemy.getHealth().getCapacity());
        assertEquals(10, enemy.getAttack());
        assertEquals(5, enemy.getDefense());
        assertEquals(20, enemy.experience());
    }

    public void testBattleWithPlayer() {
        when(mockPlayer.getPosition()).thenReturn(new Position(1, 1));
        when(mockPlayer.alive()).thenReturn(false);

        enemy.visit(mockPlayer);
        verify(mockPlayer).onDeath(); // Check that the onDeath method was called on the player
        verify(mockPlayer).swapPosition(enemy);
    }

    public void testDeath() {
        enemy.onDeath();
        // Check that a message about the enemy's death was sent
        verify(mockPlayer, never()).onDeath();
    }

    // Temporary subclass for testing abstract Enemy
    private static class TestEnemy extends Enemy {

        public TestEnemy(char tile, String name, int hitPoints, int attack, int defense, int experience) {
            super(tile, name, hitPoints, attack, defense, experience);
        }

        @Override
        public void onEnemyTurn(Player player, Board board) {
            // Implementation for test
        }

        @Override
        public void updateVisibility(Board board) {
            // Implementation for test
        }
    }
}
