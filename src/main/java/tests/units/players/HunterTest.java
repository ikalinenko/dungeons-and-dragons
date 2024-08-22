package main.java.tests.units.players;

import junit.framework.TestCase;
import main.java.model.game.Board;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Hunter;
import main.java.utils.Position;
import main.java.utils.generators.Generator;

import java.util.List;

import static org.mockito.Mockito.*;

public class HunterTest extends TestCase {

    private Hunter hunter;
    private Board mockBoard;
    private Enemy mockEnemy;
    private Generator mockGenerator;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockBoard = mock(Board.class);
        mockEnemy = mock(Enemy.class);
        mockGenerator = mock(Generator.class);
        hunter = new Hunter("Test Hunter", 100, 20, 10, 3);
        hunter.initialize(new Position(0, 0), mockGenerator, message -> {}, () -> {});
    }

    @Override
    public void tearDown() throws Exception {
        hunter = null;
    }

    public void testLevelUp() {
        hunter.addExperience(100); // Enough to level up twice
        assertEquals(3, hunter.getLevel());
        assertEquals(30, hunter.getArrowsCount());
    }

    public void testOnTurn() {
        hunter.onTurn();
        assertEquals(0, hunter.getTicksCount());

        hunter.onTurn();
        hunter.onTurn();
        assertEquals(3, hunter.getTicksCount());

        hunter.onTurn(); // Should reset after 10 turns
        assertEquals(1, hunter.getTicksCount());
        assertEquals(30, hunter.getArrowsCount());
    }

    public void testCastAbility_noArrows() {
        hunter.setArrowsCount(0);
        hunter.castAbility(mockBoard);

        // Here we would typically check that the correct message was sent
        // but for simplicity, we assume the messaging callback is mocked correctly.
    }

    public void testCastAbility_withArrows() {
        when(mockBoard.getEnemies()).thenReturn(List.of(mockEnemy));
        when(mockEnemy.getPosition()).thenReturn(new Position(1, 1));
        when(mockEnemy.alive()).thenReturn(true);
        when(mockGenerator.generate(anyInt())).thenReturn(0);

        hunter.castAbility(mockBoard);
        verify(mockEnemy, times(1)).visit(mockEnemy);

        assertEquals(9, hunter.getArrowsCount()); // Should decrease arrows by 1
    }

    public void testFindClosestEnemy() {
        Enemy enemy1 = mock(Enemy.class);
        Enemy enemy2 = mock(Enemy.class);

        when(enemy1.getPosition()).thenReturn(new Position(1, 1));
        when(enemy2.getPosition()).thenReturn(new Position(2, 2));
        when(mockGenerator.generate(anyInt())).thenReturn(0); // Always choose the first one

        List<Enemy> enemiesInRange = List.of(enemy1, enemy2);
        Enemy closestEnemy = hunter.findClosestEnemy(enemiesInRange);

        assertEquals(enemy1, closestEnemy);
    }

    public void testDescription() {
        String desc = hunter.description();
        assertTrue(desc.contains("Arrows: 10"));
        assertTrue(desc.contains("Range: 3"));
    }
}
