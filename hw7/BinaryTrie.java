import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    Node root;

    private class Node implements Comparable<Node> {
        char val;
        int frequency;
        Node left;
        Node right;

        public Node(char value, int fre, Node l, Node r) {
            val = value;
            frequency = fre;
            left = l;
            right = r;
        }

        @Override
        public int compareTo(Node o) {
            return this.frequency - o.frequency;
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> q = new MinPQ<>();

        for (char c : frequencyTable.keySet()) {
            q.insert(new Node(c, frequencyTable.get(c), null, null));
        }

        while (q.size() > 1) {
            Node min1 = q.delMin();
            Node min2 = q.delMin();
            Node temp = new Node('\0', min1.frequency + min2.frequency, min1, min2);
            q.insert(temp);
        }

        root = q.delMin();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node cur = root;

        for (int i = 0; i < querySequence.length(); i++) {
            int bit = querySequence.bitAt(i);

            if (bit == 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }

            if (cur.left == null) {
                return new Match(querySequence.firstNBits(i + 1), cur.val);
            }
        }

        return null;
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> encode = new HashMap<>();
        String path = "";
        buildCode(root, path, encode);
        return encode;
    }

    private void buildCode(Node x, String currentPath, Map<Character, BitSequence> map) {
        if (x.left == null && x.right == null) {
            map.put(x.val, new BitSequence(currentPath));
            return;
        }

        buildCode(x.left, currentPath + '0', map);
        buildCode(x.right, currentPath + '1', map);
    }
}
