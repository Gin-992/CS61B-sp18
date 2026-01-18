public class Palindrome {
    /* Given a String, wordToDeque should return a Deque. */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new LinkedListDeque<>();

        for (int i = 0; i < word.length(); i ++) {
            wordDeque.addLast(word.charAt(i));
        }

        return wordDeque;
    }

    /* return true if the given word is a palindrome, and false otherwise. */
    public boolean isPalindrome(String word) {
        Deque<Character> wordDeque = wordToDeque(word);

        while (wordDeque.size() > 1) {
            Character head = wordDeque.removeFirst();
            Character tail = wordDeque.removeLast();
            if (head != tail) {
                return false;
            }
        }

        return true;
    }

    /* return true if the word is a palindrome
     * 用参数 cc 里定义的方法来判断
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordDeque = wordToDeque(word);

        while (wordDeque.size() > 1) {
            Character head = wordDeque.removeFirst();
            Character tail = wordDeque.removeLast();
            if (!cc.equalChars(head, tail)) {
                return false;
            }
        }

        return true;
    }
}