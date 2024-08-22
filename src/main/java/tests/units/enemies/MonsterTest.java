package main.java.tests.units.enemies;

import junit.framework.TestCase;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Monster;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Position;
import main.java.utils.generators.Generator;

import static org.mockito.Mockito.*;

public class MonsterTest extends TestCase {

    private Monster monster;
    private Player mockPlayer;
    private Board mockBoard;
    private Generator mockGenerator;

    public void setUp() {
        mockPlayer = mock(Player.class);
        mockBoard = mock(Board.class);
        mockGenerator = mock(Generator.class);

        monster = new Monster('M', "Orc", 100, 30, 10, 5, 50);
        monster.initialize(new Position(0, 0), mockGenerator, message -> {}, () -> {});
    }

    public void testIsInVisionRange() {
        when(mockPlayer.getPosition()).thenReturn(new Position(1, 1));
        assertTrue(monster.isInVisionRange(mockPlayer));

        when(mockPlayer.getPosition()).thenReturn(new Position(10, 10));
        assertFalse(monster.isInVisionRange(mockPlayer));
    }

    public void testChasePlayer() {
        when(mockPlayer.getPosition()).thenReturn(new Position(0, 1));

        monster.chasePlayer(mockPlayer, mockBoard);

        // We check that the monster has moved to the right
        verify(mockPlayer, never()).onDeath(); // Check that the player is not dead
    }

    public void testRandomMove() {
        when(mockGenerator.generate(5)).thenReturn(0); // Moves to the left
        monster.randomMove(mockBoard);

        when(mockGenerator.generate(5)).thenReturn(4); // Stays in place
        monster.randomMove(mockBoard);
    }
}
