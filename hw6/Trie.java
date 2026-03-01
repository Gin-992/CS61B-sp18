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
            String word = in.readLine();
            add(word);
        }
    }

    private void add(String word) {
        Node cur = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';

            if (index < 0 || index >= 26) continue;

            if(cur.next[index] == null) {
                cur.next[index] = new Node();
            }
            cur = cur.next[index];
        }
        cur.isWord = true;
        cur.word = word;
    }

    public Node match(Node n, char c) {
        int index = c - 'a';
        if (index < 0 || index >= 26) return null;
        return n.next[index];
    }
}