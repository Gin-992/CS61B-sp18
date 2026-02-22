package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public class Board implements WorldState {
    private final int[][] tiles;
    private final int N;
    private final int BLANK = 0;

    public Board(int[][] tiles) {
        this.N = tiles.length;
        // deep copy
        this.tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, N);
        }
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i >= N || j < 0 || j >= N) {
            throw new IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    public int size() {
        return N;
    }

    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int blankRow = -1;
        int blankCol = -1;

        // 1. 找到空白块的位置
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == BLANK) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }

        // 2. 定义上下左右四个方向
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] d : dirs) {
            int neighborRow = blankRow + d[0];
            int neighborCol = blankCol + d[1];

            if (neighborRow >= 0 && neighborRow < N && neighborCol >= 0 && neighborCol < N) {
                int[][] newTiles = new int[N][N];
                for (int i = 0; i < N; i++) {
                    System.arraycopy(this.tiles[i], 0, newTiles[i], 0, N);
                }

                newTiles[blankRow][blankCol] = newTiles[neighborRow][neighborCol];
                newTiles[neighborRow][neighborCol] = BLANK;

                neighbors.enqueue(new Board(newTiles));
            }
        }
        return neighbors;
    }

    public int hamming() {
        int distance = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == BLANK) {
                    continue;
                }

                int expected = i * N + j + 1;
                if (tiles[i][j] != expected) {
                    distance++;
                }
            }
        }
        return distance;
    }

    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int val = tiles[i][j];
                if (val == BLANK) {
                    continue;
                }

                int targetRow = (val - 1) / N;
                int targetCol = (val - 1) % N;

                distance += Math.abs(i - targetRow) + Math.abs(j - targetCol);
            }
        }
        return distance;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }

        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }

        Board other = (Board) y;
        if (this.N != other.N) {
            return false;
        }

        // 深度比较数组
        return Arrays.deepEquals(this.tiles, other.tiles);
    }

    @Override
    public int hashCode() {
        // 遍历 tiles 里的每一个数字，生成一个整数
        return Arrays.deepHashCode(tiles);
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
