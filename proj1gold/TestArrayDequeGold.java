import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testArrayDeque() {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> sad2 = new ArrayDequeSolution<>();

        String log = "";
        while (true) {
            double numberBetweenZeroAndOne = StdRandom.uniform();
            int randVal = StdRandom.uniform(100);

            if (numberBetweenZeroAndOne < 0.25) {
                sad1.addFirst(randVal);
                sad2.addFirst(randVal);
                log = log + '\n' + "addFirst(" + randVal + ")";

            } else if (numberBetweenZeroAndOne < 0.5) {
                sad1.addLast(randVal);
                sad2.addLast(randVal);
                log = log + '\n' + "addLast(" + randVal + ")";

            } else if (numberBetweenZeroAndOne < 0.75) {
                if (sad1.isEmpty() && sad2.isEmpty()) {
                    continue;
                }

                Integer x = sad1.removeFirst();
                Integer y = sad2.removeFirst();
                log = log + '\n' + "removeFirst()";

                assertEquals(log, y, x);
            } else {
                if (sad1.isEmpty() && sad2.isEmpty()) {
                    continue;
                }

                Integer x = sad1.removeLast();
                Integer y = sad2.removeLast();
                log = log + '\n' + "removeLast()";

                assertEquals(log, y, x);
            }
        }
    }
}