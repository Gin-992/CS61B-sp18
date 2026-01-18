import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // 字符单引号，字符串双引号
    @Test
    public void testequalChars() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('b', 'a'));
        assertFalse(offByOne.equalChars('g', 'z'));

        assertTrue(offByOne.equalChars('A', 'B'));
        assertTrue(offByOne.equalChars('B', 'A'));
        assertFalse(offByOne.equalChars('G', 'Z'));

        assertFalse(offByOne.equalChars('a', 'B'));
        assertFalse(offByOne.equalChars('x', 'Y'));

        assertTrue(offByOne.equalChars('&', '%'));
    }
}
