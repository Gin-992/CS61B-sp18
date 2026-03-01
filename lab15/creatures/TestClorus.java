package creatures;

import huglife.*;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class TestClorus {
    @Test
    public void testAttack() {
        Clorus c = new Clorus(2);
        Clorus c2 = new Clorus(2);
        c.attack(c2);

        assertEquals(4, c.energy(), 2);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(2);
        Clorus child = c.replicate();

        assertNotSame(child, c);
        assertEquals(1, c.energy(), 1);
        assertEquals(1, child.energy(), 1);
    }

    @Test
    public void testChoose() {
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);

        Clorus c2 = new Clorus(1.8);
        HashMap<Direction, Occupant> surrounded2 = new HashMap<>();
        surrounded2.put(Direction.TOP, new Empty());
        surrounded2.put(Direction.BOTTOM, new Impassible());
        surrounded2.put(Direction.LEFT, new Impassible());
        surrounded2.put(Direction.RIGHT, new Impassible());

        Action actual2 = c2.chooseAction(surrounded2);
        Action expected2 = new Action(Action.ActionType.REPLICATE, Direction.TOP);

        assertEquals(expected2, actual2);

        Clorus c3 = new Clorus(0.8);
        HashMap<Direction, Occupant> surrounded3 = new HashMap<>();
        surrounded3.put(Direction.TOP, new Empty());
        surrounded3.put(Direction.BOTTOM, new Impassible());
        surrounded3.put(Direction.LEFT, new Impassible());
        surrounded3.put(Direction.RIGHT, new Impassible());

        Action actual3 = c3.chooseAction(surrounded3);
        Action expected3 = new Action(Action.ActionType.MOVE, Direction.TOP);

        assertEquals(expected3, actual3);

        Clorus c4 = new Clorus(2);
        HashMap<Direction, Occupant> surrounded4 = new HashMap<>();
        surrounded4.put(Direction.TOP, new Empty());
        surrounded4.put(Direction.BOTTOM, new Plip());
        surrounded4.put(Direction.LEFT, new Impassible());
        surrounded4.put(Direction.RIGHT, new Impassible());

        Action actual4 = c4.chooseAction(surrounded4);
        Action expected4 = new Action(Action.ActionType.ATTACK, Direction.BOTTOM);

        assertEquals(expected4, actual4);
    }
}
