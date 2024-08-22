package main.java.tests.units.enemies;

import junit.framework.TestCase;
import main.java.model.game.Board;
import main.java.model.tiles.Empty;
import main.java.model.tiles.units.enemies.Trap;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;

import static org.mockito.Mockito.*;

public class TrapTest extends TestCase {

    private Trap trap;
    private Player mockPlayer;
    private Board mockBoard;

    public void setUp() {
        mockPlayer = mock(Player.class);
        mockBoard = mock(Board.class);

        trap = new Trap('T', "Spiked Trap", 30, 10, 5, 20, 3, 2);
        trap.setPosition(new Position(0, 0));

    }

    public void testIsVisible() {
        assertTrue(trap.isVisible());

        trap.onEnemyTurn(mockPlayer, mockBoard);
        trap.onEnemyTurn(mockPlayer, mockBoard);
        trap.onEnemyTurn(mockPlayer, mockBoard);
        assertFalse(trap.isVisible());

        trap.onEnemyTurn(mockPlayer, mockBoard);
        trap.onEnemyTurn(mockPlayer, mockBoard);
        assertTrue(trap.isVisible());
    }

    public void testOnEnemyTurn_Visible() {
        when(mockPlayer.getPosition()).thenReturn(new Position(1, 1));

        trap.onEnemyTurn(mockPlayer, mockBoard);
        verify(mockPlayer).defend();  // Check if the player is under attack
    }

    public void testOnEnemyTurn_NotVisible() {
        trap.onEnemyTurn(mockPlayer, mockBoard);
        trap.onEnemyTurn(mockPlayer, mockBoard);
        trap.onEnemyTurn(mockPlayer, mockBoard);

        when(mockPlayer.getPosition()).thenReturn(new Position(1, 1));

        trap.onEnemyTurn(mockPlayer, mockBoard);
        verify(mockPlayer, never()).defend();  // The trap is invisible, the attack should not happen.
    }

    public void testUpdateVisibility() {
        trap.updateVisibility(mockBoard);
        assertEquals('T', trap.getTile());

        trap.onEnemyTurn(mockPlayer, mockBoard);
        trap.onEnemyTurn(mockPlayer, mockBoard);
        trap.onEnemyTurn(mockPlayer, mockBoard);

        trap.updateVisibility(mockBoard);
        assertEquals(Empty.getEmptyTile(), trap.getTile()); // Check that the tile has become empty
    }

    public void testIsInRange() {
        when(mockPlayer.getPosition()).thenReturn(new Position(0, 1));
        assertTrue(trap.isInRange(mockPlayer));

        when(mockPlayer.getPosition()).thenReturn(new Position(3, 3));
        assertFalse(trap.isInRange(mockPlayer));
    }
}
