package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF uf;      // 用于判断系统是否渗透 (Percolates)
    private WeightedQuickUnionUF ufFull;  // 用于判断格子是否满水 (isFull)
    private int size;
    private int topNode;
    private int bottomNode;
    private int openSiteCount;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be > 0");
        }
        this.size = N;
        this.openSiteCount = 0;
        this.grid = new boolean[N][N];

        int totalNodes = N * N;
        this.topNode = totalNodes;
        this.bottomNode = totalNodes + 1;

        // 初始化两个并查集
        this.uf = new WeightedQuickUnionUF(totalNodes + 2);
        this.ufFull = new WeightedQuickUnionUF(totalNodes + 1);
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        // 1. 检查索引是否越界
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        // 2. 如果已经打开，直接返回
        if (isOpen(row, col)) {
            return;
        }

        // 3. 打开格子
        grid[row][col] = true;
        openSiteCount++;

        int currentID = xyTo1D(row, col);

        // 4. 如果在第一行，连接到虚拟顶部
        if (row == 0) {
            uf.union(currentID, topNode);
            ufFull.union(currentID, topNode);
        }

        // 5. 如果在最后一行，连接到虚拟底部
        if (row == size - 1) {
            uf.union(currentID, bottomNode);
        }

        // 6. 尝试连接上下左右的邻居
        // 上
        if (row > 0 && isOpen(row - 1, col)) {
            int upID = xyTo1D(row - 1, col);
            uf.union(currentID, upID);
            ufFull.union(currentID, upID);
        }
        // 下
        if (row < size - 1 && isOpen(row + 1, col)) {
            int downID = xyTo1D(row + 1, col);
            uf.union(currentID, downID);
            ufFull.union(currentID, downID);
        }
        // 左
        if (col > 0 && isOpen(row, col - 1)) {
            int leftID = xyTo1D(row, col - 1);
            uf.union(currentID, leftID);
            ufFull.union(currentID, leftID);
        }
        // 右
        if (col < size - 1 && isOpen(row, col + 1)) {
            int rightID = xyTo1D(row, col + 1);
            uf.union(currentID, rightID);
            ufFull.union(currentID, rightID);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }
        if (!isOpen(row, col)) {
            return false;
        }
        return ufFull.find(topNode) == ufFull.find(xyTo1D(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSiteCount;
    }

    // does the system percolate?
    public boolean percolates() {
        if (size == 1) {
            return isOpen(0, 0);
        }
        return uf.find(topNode) == uf.find(bottomNode);
    }

    // 辅助方法：将 2D 坐标转为 1D 索引
    private int xyTo1D(int row, int col) {
        return row * size + col;
    }

    public static void main(String[] args) {
        // 单元测试代码 (可选)
    }
}