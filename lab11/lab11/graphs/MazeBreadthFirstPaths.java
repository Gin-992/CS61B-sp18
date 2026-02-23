package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;
    Queue<Integer> fringe = new ArrayDeque<>();

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        marked[s] = true;
        distTo[s] = 0;
        edgeTo[s] = s;
        fringe.add(s);
        announce();
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        while (!fringe.isEmpty()) {
            int re = fringe.poll();

            for (int nei : maze.adj(re)) {
                if (!marked[nei]) {
                    marked[nei] = true;
                    distTo[nei] = distTo[re] + 1;
                    edgeTo[nei] = re;
                    fringe.add(nei);
                    announce();

                    if (nei == t) {
                        return;
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

