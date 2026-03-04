public class Trie {
    private String path;
    public Node root;

    public class Node {
        boolean isWord;
        String word;
        Node[] liked;

        public Node() {
            isWord = false;
            liked = new Node[26];
        }
    }

    public Trie(String dictPath) {
        path = dictPath;
        root = new Node();
        In in = new In(path);
        while (in.hasNextLine()) {
            insert(in.readLine());
        }
    }

    private void insert(String word) {
        int N = word.length();
        Node cur = root;

        for (int i = 0; i < N; i++) {
            char c = word.charAt(i);
            char cc = Character.toLowerCase(c);

            int index = cc - 'a';
            if (cc < 'a' || cc > 'z') {
                return;
            }
            if (cur.liked[index] == null) {
                cur.liked[index] = new Node();
            }

            cur = cur.liked[index];
        }

        cur.isWord = true;
        cur.word = word.toLowerCase();
    }

    public boolean contains(String word) {
        Node cur = root;
        int N = word.length();

        for (int i = 0; i < N; i++) {
            char c = word.charAt(i);
            char cc = Character.toLowerCase(c);
            int index = cc - 'a';

            if (cur.liked[index] == null) {
                return false;
            }
            cur = cur.liked[index];
        }

        return cur.isWord;
    }

    public boolean hasPrefix(String s) {
        Node cur = root;
        int N = s.length();

        for (int i = 0; i < N; i++) {
            char c = s.charAt(i);
            char cc = Character.toLowerCase(c);
            int index = cc - 'a';

            if (cur.liked[index] == null) {
                return false;
            }
            cur = cur.liked[index];
        }

        return true;
    }
}