import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        char[][] board = load(boardFilePath);
        Trie trie = new Trie(dictPath);
        Set<String> words = new HashSet<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boolean[][] visited = new boolean[board.length][board[0].length];
                char c = board[i][j];
                int index = c - 'a';

                if (trie.root.liked[index] != null) {
                    dfs(board, i, j, trie.root.liked[index], visited, words);
                }
            }
        }

        List<String> reWords = new ArrayList<>(words);

        reWords.sort((a, b) -> {
            if (b.length() != a.length()) {
                return b.length() - a.length();
            }
            return a.compareTo(b);
        });

        if (reWords.size() > k) {
            return reWords.subList(0, k);
        }
        return reWords;
    }

    private static char[][] load(String boardFilePath) {
        In in = new In(boardFilePath);
        String[] allWords = in.readAllLines();

        int rows = allWords.length;
        int cols = allWords[0].length();
        char[][] board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            if (allWords[i].length() != cols) {
                throw new IllegalArgumentException("Board should be rectangle");
            }

            for (int j = 0; j < allWords[i].length(); j++) {
                char c = Character.toLowerCase(allWords[i].charAt(j));
                board[i][j] = c;
            }
        }

        return board;
    }

    private static void dfs(char[][] board, int i, int j, Trie.Node node,
                            boolean[][] visited, Set<String> words) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return;
        }

        if (visited[i][j]) {
            return;
        }

        char c = board[i][j];
        int index = Character.toLowerCase(c) - 'a';
        Trie.Node next = node.liked[index];
        // 剪枝
        if (next == null) {
            return;
        }

        visited[i][j] = true;
        if (next.isWord) {
            words.add(next.word);
        }

        dfs(board, i + 1, j, next, visited, words);
        dfs(board, i - 1, j, next, visited, words);
        dfs(board, i, j + 1, next, visited, words);
        dfs(board, i, j - 1, next, visited, words);
        dfs(board, i + 1, j + 1, next, visited, words);
        dfs(board, i - 1, j - 1, next, visited, words);
        dfs(board, i - 1, j + 1, next, visited, words);
        dfs(board, i + 1, j - 1, next, visited, words);

        visited[i][j] = false;
    }
}
