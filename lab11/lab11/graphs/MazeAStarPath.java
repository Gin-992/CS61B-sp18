package lab11.graphs;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private int targetX, targetY;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        this.targetX = targetX;
        this.targetY = targetY;

        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int vX = maze.toX(v);
        int vY = maze.toY(v);
        return Math.abs(vX - targetX) + Math.abs(vY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        // 外在优先级
        IndexMinPQ<Integer> pq = new IndexMinPQ<>(maze.V());
        pq.insert(s, distTo[s] + h(s));

        while (!pq.isEmpty()) {
            // best first search
            int v = pq.delMin();

            marked[v] = true;
            announce();

            if (v == t) {
                targetFound = true;
                return;
            }

            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    // edge relaxation: add edge to the SPT if it yields better distance.
                    if (distTo[v] + 1 < distTo[w]) {
                        distTo[w] = distTo[v] + 1;
                        edgeTo[w] = v;
                        announce();

                        int priority = distTo[w] + h(w);
                        if (pq.contains(w)) {
                            pq.changeKey(w, priority);
                        } else {
                            pq.insert(w, priority);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

