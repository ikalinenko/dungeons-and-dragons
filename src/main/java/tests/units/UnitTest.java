package main.java.tests.units;

import junit.framework.TestCase;
import main.java.model.game.Board;
import main.java.model.tiles.Empty;
import main.java.model.tiles.Wall;
import main.java.model.tiles.units.Unit;
import main.java.model.tiles.units.enemies.Enemy;
import main.java.model.tiles.units.players.Player;
import main.java.utils.Health;
import main.java.utils.Position;
import main.java.utils.callbacks.DeathCallBack;
import main.java.utils.callbacks.MessageCallBack;
import main.java.utils.generators.Generator;

import static org.mockito.Mockito.*;

public class UnitTest extends TestCase {

    private Unit unit;
    private Board mockBoard;
    private Generator mockGenerator;
    private MessageCallBack mockMessageCallBack;
    private DeathCallBack mockDeathCallBack;

    public void setUp() {
        mockBoard = mock(Board.class);
        mockGenerator = mock(Generator.class);
        mockMessageCallBack = mock(MessageCallBack.class);
        mockDeathCallBack = mock(DeathCallBack.class);

        unit = new TestUnit('U', "Test Unit", 100, 20, 10);
        unit.initialize(new Position(0, 0), mockGenerator, mockMessageCallBack, mockDeathCallBack);
    }

    public void testInitialization() {
        assertEquals("Test Unit", unit.getName());
        assertEquals(100, unit.getHealth().getCapacity());
        assertEquals(20, unit.getAttack());
        assertEquals(10, unit.getDefense());
        assertEquals(new Position(0, 0), unit.getPosition());
    }

    public void testAttack() {
        when(mockGenerator.generate(20)).thenReturn(15);
        int attackValue = unit.attack();
        assertEquals(15, attackValue);
    }

    public void testDefend() {
        when(mockGenerator.generate(10)).thenReturn(5);
        int defenseValue = unit.defend();
        assertEquals(5, defenseValue);
    }

    public void testBattle() {
        Unit mockDefender = mock(Unit.class);
        when(mockDefender.defend()).thenReturn(5);
        when(mockDefender.getHealth()).thenReturn(new Health(50));
        when(mockGenerator.generate(20)).thenReturn(15);

        unit.battle(mockDefender);

        verify(mockMessageCallBack).send(anyString());  // Check that the message was sent
        verify(mockDefender.getHealth()).takeDamage(10);  // Check that the damage was done (15 - 5 = 10)
    }

    public void testMove() {
        Position newPosition = new Position(1, 0);
        when(mockBoard.isPositionFree(newPosition)).thenReturn(true);

        unit.move(newPosition, mockBoard);

        assertEquals(newPosition, unit.getPosition());
        verify(mockBoard).updateTile(new Position(0, 0), new Empty());  // Check that the old position has been cleared
        verify(mockBoard).updateTile(newPosition, unit);  // Check that the new position has been occupied by a unit
    }

    public void testMoveInvalid() {
        Position invalidPosition = new Position(-1, 0);
        when(mockBoard.isPositionFree(invalidPosition)).thenReturn(false);

        unit.move(invalidPosition, mockBoard);

        assertEquals(new Position(0, 0), unit.getPosition());  // The position should not change.
    }

    public void testVisitEmpty() {
        Empty emptyTile = new Empty();
        unit.visit(emptyTile);

        verify(mockBoard).updateTile(new Position(0, 0), emptyTile);  // Check that the unit's position is updated
    }

    public void testVisitWall() {
        Wall wallTile = new Wall();
        unit.visit(wallTile);

        // Nothing should happen when visiting the wall
        assertEquals(new Position(0, 0), unit.getPosition());  // The position should not change.
    }

    public void testOnDeath() {
        unit.onDeath();

        verify(mockDeathCallBack).onDeath();  // Check that the death callback has been called
    }

    // Temporary subclass for testing abstract Unit
    private static class TestUnit extends Unit {

        public TestUnit(char tile, String name, int hitPoints, int attack, int defense) {
            super(tile, name, hitPoints, attack, defense);
        }

        @Override
        public void accept(Unit unit) {
            // Implementation for test
        }

        @Override
        public void visit(Player p) {
            // Implementation for test
        }

        @Override
        public void visit(Enemy e) {
            // Implementation for test
        }
    }
}
