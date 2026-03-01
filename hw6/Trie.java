import edu.princeton.cs.algs4.In;

public class Trie {
    public Node root;

    public static class Node {
        public boolean isWord;
        public String word;
        public Node[] next = new Node[26];
    }

    public Trie(String dictPath) {
        root = new Node();
        In in = new In(dictPath);
        while (in.hasNextLine()) {
            String rawWord = in.readLine();
            // 清洗并添加单词
            add(rawWord);
        }
    }

    private void add(String rawWord) {
        String cleanWord = rawWord.toLowerCase();
        if (cleanWord.length() < 3) return;

        for (int i = 0; i < cleanWord.length(); i++) {
            char c = cleanWord.charAt(i);
            if (c < 'a' || c > 'z') {
                return; // 发现非法字符，整个单词都不加入
            }
        }

        Node cur = root;
        for (int i = 0; i < cleanWord.length(); i++) {
            int index = cleanWord.charAt(i) - 'a';
            if (cur.next[index] == null) {
                cur.next[index] = new Node();
            }
            cur = cur.next[index];
        }
        cur.isWord = true;
        cur.word = cleanWord;
    }

    public Node match(Node n, char c) {
        if (c < 'a' || c > 'z') return null;
        return n.next[c - 'a'];
    }
}