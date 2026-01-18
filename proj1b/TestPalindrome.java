import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        String word1 = "";
        String word2 = "a";
        String word3 = "noon";
        String word4 = "car";

        assertTrue(palindrome.isPalindrome(word1));
        assertTrue(palindrome.isPalindrome(word2));
        assertTrue(palindrome.isPalindrome(word3));
        assertFalse(palindrome.isPalindrome(word4));
    }

    @Test
    public void testIsPalindromeOffByOne() {
        CharacterComparator cc = new OffByOne();

        String word1 = "";
        String word2 = "a";
        // "flake": f-e差1, l-k差1
        String word3 = "flake";
        String word4 = "noon";

        assertTrue(palindrome.isPalindrome(word1, cc));
        assertTrue(palindrome.isPalindrome(word2, cc));
        assertTrue(palindrome.isPalindrome(word3, cc));
        assertFalse(palindrome.isPalindrome(word4, cc));
    }
}
