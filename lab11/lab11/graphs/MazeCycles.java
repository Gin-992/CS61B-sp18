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
    private boolean cycleFound = false;
    private int[] cameFrom;

    public MazeCycles(Maze m) {
        super(m);
        cameFrom = new int[m.V()];
        maze = m;
    }

    @Override
    public void solve() {
        marked[0] = true;
        announce();
        cameFrom[0] = 0;
        dfs(0);
    }

    private void dfs(int s) {
        for (int w : maze.adj(s)) {
            if (!marked[w]) {
                marked[w] = true;
                announce();
                cameFrom[w] = s;
                dfs(w);

                if (cycleFound) {
                    return;
                }
            }

            else if (marked[w] && cameFrom[s] != w) {
                cycleFound = true;

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
