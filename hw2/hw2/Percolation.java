package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] map;
    private WeightedQuickUnionUF WQF;
    private WeightedQuickUnionUF rawWQF;
    private int size;
    private int openSites;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Input should larger than 0.");
        }

        map = new boolean[N][N];

        rawWQF = new WeightedQuickUnionUF(N * N + 1);
        WQF = new WeightedQuickUnionUF(N * N + 2);
        size = N;
        openSites = 0;
    }

    private int xyTo1D(int row, int col) {
        return row * size + col + 1;
    }

    private void isValid(int row, int col) {
        if (row < size && col < size && row >= 0 && col >= 0) {
            return;
        }
        throw new IndexOutOfBoundsException("Crossed the boundary.");
    }

    public void open(int row, int col) {
        isValid(row, col);

        if (!isOpen(row, col)) {
            map[row][col] = true;
            openSites += 1;

            int pos = xyTo1D(row, col);

            if (row == 0) {
                WQF.union(pos, 0);
                rawWQF.union(pos, 0);
            }

            if (row == size - 1) {
                WQF.union(pos, size * size + 1);
            }

            if (row - 1 >= 0 && map[row - 1][col]) {
                WQF.union(pos, xyTo1D(row - 1, col));
                rawWQF.union(pos, xyTo1D(row - 1, col));
            }
            if (row + 1 < size && map[row + 1][col]) {
                WQF.union(pos, xyTo1D(row + 1, col));
                rawWQF.union(pos, xyTo1D(row + 1, col));
            }
            if (col - 1 >= 0 && map[row][col - 1]) {
                WQF.union(pos, xyTo1D(row, col - 1));
                rawWQF.union(pos, xyTo1D(row, col - 1));
            }
            if (col + 1 < size && map[row][col + 1]) {
                WQF.union(pos, xyTo1D(row, col + 1));
                rawWQF.union(pos, xyTo1D(row, col + 1));
            }
        }
    }

    public boolean isOpen(int row, int col) {
        isValid(row, col);
        return map[row][col];
    }

    public boolean isFull(int row, int col) {
        isValid(row, col);
        return rawWQF.connected(0, xyTo1D(row, col));
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return WQF.connected(0, size * size + 1);
    }

    public static void main(String[] arg) {
        // To pass the test.
    }
}