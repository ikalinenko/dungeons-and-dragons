package main.java.tests.units.players;

import junit.framework.TestCase;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Mage;
import main.java.utils.Health;
import main.java.utils.Position;
import main.java.utils.generators.Generator;

import static org.mockito.Mockito.*;

import java.util.List;

public class MageTest extends TestCase {

    private Mage mage;
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

        mage = new Mage("Gandalf", 100, 20, 10, 200, 50, 30, 3, 5);
        mage.initialize(new Position(0, 0), mockGenerator, message -> {}, () -> {});
    }

    public void testLevelUp() {
        mage.addExperience(100); // Suppose it's enough to level up
        assertEquals(2, mage.getLevel());
        assertEquals(250, mage.getManaPool());
        assertEquals(60, mage.getSpellPower());
        assertTrue(mage.getCurrentMana() <= mage.getManaPool()); // Checking that the current mana does not exceed the mana pool
    }

    public void testOnTurn() {
        int initialMana = mage.getCurrentMana();
        mage.onTurn();
        assertEquals(initialMana + 1, mage.getCurrentMana());

        // Check that if the ability was used, the mana does not increase
        mage.castAbility(mockBoard); // Let's assume the ability was used
        mage.onTurn();
        assertEquals(initialMana + 1, mage.getCurrentMana());
    }

    public void testCastAbility_NotEnoughMana() {
        mage.setCurrentMana(10); // Setting insufficient mana
        mage.castAbility(mockBoard);

        // Check that the ability has not been used
        assertFalse(mage.isAbilityUsedThisTurn());
    }

    public void testCastAbility_EnoughMana() {
        when(mockBoard.getEnemies()).thenReturn(List.of(mockEnemy1, mockEnemy2));
        when(mockGenerator.generate(anyInt())).thenReturn(0);

        mage.castAbility(mockBoard);

        verify(mockHealth1).takeDamage(anyInt()); // Check that the damage was done to the first enemy
        verify(mockHealth2, never()).takeDamage(anyInt()); // The second enemy was not damaged (according to the conditions)
        assertEquals(mage.getManaPool() - 50, mage.getCurrentMana()); // Check that the mana has decreased
    }
}
