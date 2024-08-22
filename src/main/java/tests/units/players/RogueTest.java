package main.java.tests.units.players;

import junit.framework.TestCase;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Rogue;
import main.java.utils.Health;
import main.java.utils.Position;
import main.java.utils.generators.Generator;

import static org.mockito.Mockito.*;

import java.util.List;

public class RogueTest extends TestCase {

    private Rogue rogue;
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
        when(mockEnemy2.getPosition()).thenReturn(new Position(3, 3));
        when(mockEnemy1.getHealth()).thenReturn(mockHealth1);
        when(mockEnemy2.getHealth()).thenReturn(mockHealth2);

        rogue = new Rogue("Shadow", 100, 20, 10, 50);
        rogue.initialize(new Position(0, 0), mockGenerator, message -> {}, () -> {});
    }

    public void testLevelUp() {
        rogue.addExperience(100); // Suppose it's enough to level up
        assertEquals(2, rogue.getLevel());
        assertEquals(100, rogue.getCurrentEnergy()); // Checking that the energy is set to maximum
        assertTrue(rogue.getAttack() > 20); // Check that the attack has increased
    }

    public void testOnTurn() {
        int initialEnergy = rogue.getCurrentEnergy();
        rogue.onTurn();
        assertEquals(Math.min(initialEnergy + 10, 100), rogue.getCurrentEnergy());

        // We check that if the ability was used, the energy is restored
        rogue.castAbility(mockBoard); // Let's assume the ability was used.
        rogue.onTurn();
        assertEquals(Math.min(initialEnergy + 10 - 50 + 10, 100), rogue.getCurrentEnergy());
    }

    public void testCastAbility_NotEnoughEnergy() {
        rogue.setCurrentEnergy(10); // We are setting insufficient energy
        rogue.castAbility(mockBoard);

        // Check that the ability has not been used
        assertFalse(rogue.isAbilityUsedThisTurn());
    }

    public void testCastAbility_EnoughEnergy() {
        when(mockBoard.getEnemies()).thenReturn(List.of(mockEnemy1, mockEnemy2));

        rogue.castAbility(mockBoard);

        verify(mockHealth1).takeDamage(anyInt()); // Check that the damage was done to the first enemy
        verify(mockHealth2, never()).takeDamage(anyInt()); // The second enemy was not damaged (according to the conditions)
        assertEquals(50, rogue.getCurrentEnergy()); // We check that the energy has decreased
    }
}
