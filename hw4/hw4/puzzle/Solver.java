package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    private int finalMove;
    private List<WorldState> path;

    private class SearchNode implements Comparable<SearchNode> {
        WorldState cur;
        int move;
        SearchNode pre;
        int priority;

        SearchNode(WorldState c, SearchNode p, int m) {
            cur = c;
            pre = p;
            move = m;
            priority = m + c.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(SearchNode o) {
            return this.priority - o.priority;
        }
    }

    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        path = new ArrayList<>();

        pq.insert(new SearchNode(initial, null, 0));

        while (!pq.isEmpty()) {
            SearchNode del = pq.delMin();

            if (del.cur.isGoal()) {
                finalMove = del.move;

                SearchNode node = del;
                while (node != null) {
                    path.add(node.cur);
                    node = node.pre;
                }

                Collections.reverse(path);
                return;
            }

            for (WorldState w : del.cur.neighbors()) {
                if (del.pre != null && del.pre.cur.equals(w)) {
                    continue;
                }

                pq.insert(new SearchNode(w, del, del.move + 1));
            }
        }
    }

    public int moves() {
        return finalMove;
    }

    public Iterable<WorldState> solution() {
        return path;
    }

}
