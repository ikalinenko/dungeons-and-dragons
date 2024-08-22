package main.java.tests.units.players;

import junit.framework.TestCase;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Warrior;
import main.java.utils.Health;
import main.java.utils.Position;
import main.java.utils.generators.Generator;

import static org.mockito.Mockito.*;

import java.util.List;

public class WarriorTest extends TestCase {

    private Warrior warrior;
    private Board mockBoard;
    private Enemy mockEnemy1;
    private Enemy mockEnemy2;
    private Health mockHealth1;
    private Health mockHealth2;
    private Generator mockGenerator;

    public void setUp() {
        mockBoard = mock(Board.class);
        mockEnemy1 = mock(Enemy.class);
        mockEnemy2 = mock(Enemy.class);
        mockHealth1 = mock(Health.class);
        mockHealth2 = mock(Health.class);
        mockGenerator = mock(Generator.class);

        when(mockEnemy1.getPosition()).thenReturn(new Position(1, 1));
        when(mockEnemy2.getPosition()).thenReturn(new Position(4, 4));
        when(mockEnemy1.getHealth()).thenReturn(mockHealth1);
        when(mockEnemy2.getHealth()).thenReturn(mockHealth2);

        warrior = new Warrior("Arthur", 200, 30, 15, 3);
        warrior.initialize(new Position(0, 0), mockGenerator, message -> {}, () -> {});
    }

    public void testLevelUp() {
        warrior.addExperience(100); // Suppose it's enough to level up
        assertEquals(2, warrior.getLevel());
        assertEquals(0, warrior.getRemainingCooldown()); // Check that the cooldown is reset
        assertTrue(warrior.getHealth().getCapacity() > 200); // Check that health has increased
    }

    public void testOnTurn() {
        warrior.castAbility(mockBoard); // Use the ability to activate the recharge
        warrior.onTurn();
        assertEquals(2, warrior.getRemainingCooldown()); // Check that the cooldown has decreased by 1

        // We check that if the ability was not used, the cooldown does not change
        warrior.onTurn();
        assertEquals(1, warrior.getRemainingCooldown());
    }

    public void testCastAbility_OnCooldown() {
        warrior.castAbility(mockBoard); // Use the ability to activate the cooldown
        warrior.castAbility(mockBoard); // Attempt to reuse on cooldown

        // Check that the ability has not been used again
        verify(mockHealth1, never()).takeDamage(anyInt());
        assertEquals(2, warrior.getRemainingCooldown());
    }

    public void testCastAbility_Available() {
        when(mockBoard.getEnemies()).thenReturn(List.of(mockEnemy1, mockEnemy2));
        when(mockGenerator.generate(anyInt())).thenReturn(0);

        warrior.castAbility(mockBoard);

        // Check that the warrior's health has increased
        int expectedHeal = 10 * warrior.getDefense();
        verify(mockHealth1).heal(eq(expectedHeal));

        // Check that damage was dealt to the enemy
        verify(mockHealth1).takeDamage(anyInt());
        verify(mockHealth2, never()).takeDamage(anyInt());
        assertEquals(3, warrior.getRemainingCooldown());
    }
}
