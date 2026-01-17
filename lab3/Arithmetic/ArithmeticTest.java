import static org.junit.Assert.*;
import org.junit.Test;

public class ArithmeticTest {

    /** Performs a few arbitrary tests to see if the product method is correct */

    @Test
    // 所有测试都必须是非静态的。
    public void testProduct() {
        /* assertEquals for comparison of ints takes two arguments:
        assertEquals(expected, actual).
        if it is false, then the assertion will be false, and this test will fail.
        */

        assertEquals(30, Arithmetic.product(5, 6));
        assertEquals(-30, Arithmetic.product(5, -6));
        assertEquals(0, Arithmetic.product(0, -6));
    }

    /** Performs a few arbitrary tests to see if the sum method is correct */

    @Test 
    public void testSum() {

        // 短路测试—：一旦方法中的某个断言失败，它就会输出失败结果并继续执行下一个测试。
        assertEquals(11, Arithmetic.sum(5, 6));
        assertEquals(-1, Arithmetic.sum(5, -6));
        assertEquals(-6, Arithmetic.sum(0, -6));
        assertEquals(0, Arithmetic.sum(6, -6));
    }

    /* Run the unit tests in this file. */
    public static void main(String... args) {        
        jh61b.junit.TestRunner.runTests("all", ArithmeticTest.class);
    }
}
