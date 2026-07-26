import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Trie {
    static class Node {
        Map<Character, Node> children;
        Set<String> ogName;

        Node() {
            children = new HashMap<>();
            ogName = new HashSet<>();
        }
    }

    private Node root = new Node();

    public void put(String cleanedName, String ogName) {
        Node cur = root;

        for (int i = 0; i < cleanedName.length(); i++) {
            if (!cur.children.containsKey(cleanedName.charAt(i))) {
                cur.children.put(cleanedName.charAt(i), new Node());
            }
            cur = cur.children.get(cleanedName.charAt(i));
        }

        cur.ogName.add(ogName);
    }

    public List<String> getKeysByPrefix(String prefix) {
        Node cur = root;
        for (int i = 0; i < prefix.length(); i++) {
            if (!cur.children.containsKey(prefix.charAt(i))) {
                return new ArrayList<>();
            }
            cur = cur.children.get(prefix.charAt(i));
        }

        List<String> results = new ArrayList<>();
        helper(cur, results);
        return results;
    }

    private void helper(Node node, List<String> results) {
        results.addAll(node.ogName);
        for (Node child : node.children.values()) {
            helper(child, results);
        }
    }
}