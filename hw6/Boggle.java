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
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive");
        }

        Trie trie = new Trie(dictPath);
        char[][] board = loadBoard(boardFilePath);

        int N = board.length;
        int M = board[0].length;
        boolean[][] visited = new boolean[N][M];

        Set<String> words = new HashSet<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                dfs(board, i, j, trie.root, visited, words);
            }
        }

        List<String> sortedWords = new ArrayList<>(words);
        sortedWords.sort((a, b) -> {
            int lenDiff = b.length() - a.length();
            if (lenDiff != 0) {
                return lenDiff;
            }
            // 首字母
            return a.compareTo(b);
        });

        int limit = Math.min(k, sortedWords.size());
        return sortedWords.subList(0, limit);
    }

    private static char[][] loadBoard(String path) {
        In in = new In(path);
        if (!in.exists()) {
            throw new IllegalArgumentException("Board file not found");
        }

        String[] lines = in.readAllLines();
        int rows = lines.length;
        int cols = lines[0].length();

        char[][] board = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            String line = lines[i];
            if (line.length() != cols) {
                throw new IllegalArgumentException("Board is not rectangular");
            }
            for (int j = 0; j < cols; j++) {
                board[i][j] = lines[i].charAt(j);
            }
        }

        return board;
    }

    private static void dfs(char[][] board, int i, int j, Trie.Node currNode,
                            boolean[][] visited, Set<String> words) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return;
        }

        if (visited[i][j]) {
            return;
        }

        char c = board[i][j];

        Trie.Node nextNode = currNode.next[c - 'a'];

        if (nextNode == null) {
            return;
        }

        if (nextNode.isWord) {
            if (nextNode.word.length() >= 3) {
                words.add(nextNode.word);
            }
        }

        visited[i][j] = true;

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }
                dfs(board, i + rowOffset, j + colOffset, nextNode, visited, words);
            }
        }

        visited[i][j] = false;
    }
}
