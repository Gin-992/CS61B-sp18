// 导入静态方法和变量
import static org.junit.Assert.*;

import org.junit.Test;

public class FlikTest {
    @Test
    public void testFlik() {
        assertTrue(Flik.isSameNumber(1, 1));
        assertTrue(Flik.isSameNumber(65, 65));
        assertTrue(Flik.isSameNumber(127, 127));
        assertTrue(Flik.isSameNumber(128, 128));
    }
}
