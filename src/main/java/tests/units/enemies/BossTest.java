package main.java.tests.units.enemies;

import junit.framework.TestCase;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Boss;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Health;
import main.java.utils.Position;
import main.java.utils.generators.Generator;

import static org.mockito.Mockito.*;

public class BossTest extends TestCase {

    private Boss boss;
    private Player mockPlayer;
    private Board mockBoard;
    private Generator mockGenerator;

    public void setUp() {
        mockPlayer = mock(Player.class);
        mockBoard = mock(Board.class);
        mockGenerator = mock(Generator.class);

        boss = new Boss('B', "Dragon", 300, 50, 20, 10, 100, 3);
        boss.initialize(new Position(0, 0), mockGenerator, message -> {}, () -> {});
    }

    public void testOnEnemyTurn_CastAbility() {
        when(mockPlayer.getPosition()).thenReturn(new Position(0, 1));
        when(mockBoard.getPlayer()).thenReturn(mockPlayer);

        boss.onEnemyTurn(mockPlayer, mockBoard);
        boss.onEnemyTurn(mockPlayer, mockBoard);
        boss.onEnemyTurn(mockPlayer, mockBoard);

        verify(mockPlayer.getHealth()).takeDamage(anyInt()); // Check that damage was done
        assertEquals(0, boss.getCombatTicks()); // Check that combatTicks is reset
    }

    public void testOnEnemyTurn_ChasePlayer() {
        when(mockPlayer.getPosition()).thenReturn(new Position(0, 1));
        when(mockBoard.getPlayer()).thenReturn(mockPlayer);

        boss.onEnemyTurn(mockPlayer, mockBoard);
        boss.onEnemyTurn(mockPlayer, mockBoard);

        verify(mockPlayer.getHealth(), never()).takeDamage(anyInt()); // Damage is not dealt until the ability is activated.
        assertEquals(2, boss.getCombatTicks()); // Check that combatTicks is increasing
    }

    public void testOnEnemyTurn_RandomMove() {
        when(mockPlayer.getPosition()).thenReturn(new Position(10, 10)); // Player out of sight

        boss.onEnemyTurn(mockPlayer, mockBoard);

        verify(mockPlayer.getHealth(), never()).takeDamage(anyInt()); // No damage is dealt because the player is out of sight.
        assertEquals(0, boss.getCombatTicks()); // combatTicks reset
    }

    public void testCastAbility() {
        when(mockBoard.getPlayer()).thenReturn(mockPlayer);
        when(mockPlayer.getPosition()).thenReturn(new Position(0, 1));
        when(mockPlayer.defend()).thenReturn(10);
        when(mockPlayer.getHealth()).thenReturn(mock(Health.class));

        boss.castAbility(mockBoard);

        verify(mockPlayer.getHealth()).takeDamage(anyInt()); // Checking that the damage was done
    }
}
