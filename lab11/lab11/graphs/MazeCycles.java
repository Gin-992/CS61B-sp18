package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private boolean findCycle = false;
    private int[] cameFrom;

    public MazeCycles(Maze m) {
        super(m);
        cameFrom = new int[m.V()];
        maze = m;
    }

    @Override
    public void solve() {
        marked[0] = true;
        distTo[0] = 0;
        cameFrom[0] = 0;
        dfs(0);
    }

    private void dfs(int s) {
        for (int w : maze.adj(s)) {
            if (!marked[w]) {
                marked[w] = true;
                cameFrom[w] = s;
                distTo[w] = distTo[s] + 1;
                announce();
                dfs(w);

                if (findCycle) {
                    return;
                }
            } else if (w != cameFrom[s] && marked[w]) {
                findCycle = true;

                edgeTo[w] = s;
                announce();

                int cur = s;
                while (cur != w) {
                    edgeTo[cur] = cameFrom[cur];
                    announce();
                    cur = cameFrom[cur];
                }
                return;
            }
        }
    }
}
