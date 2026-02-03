package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF uf;      // 用于判断系统是否渗透 (Percolates)
    private WeightedQuickUnionUF ufFull;  // 用于判断格子是否满水 (isFull) - 防止回流
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

        // 索引 0 到 N^2-1 是格子
        // N^2 是虚拟顶部 (Virtual Top)
        // N^2 + 1 是虚拟底部 (Virtual Bottom)
        int totalNodes = N * N;
        this.topNode = totalNodes;
        this.bottomNode = totalNodes + 1;

        // 初始化两个并查集
        this.uf = new WeightedQuickUnionUF(totalNodes + 2);     // 带头带尾
        this.ufFull = new WeightedQuickUnionUF(totalNodes + 1); // 只带头
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        if (isOpen(row, col)) {
            return;
        }

        // 1. 打开格子
        grid[row][col] = true;
        openSiteCount++;

        int currentID = xyTo1D(row, col);

        // 2. 如果在第一行，连接到虚拟顶部
        if (row == 0) {
            uf.union(currentID, topNode);
            ufFull.union(currentID, topNode);
        }

        // 3. 如果在最后一行，连接到虚拟底部 (注意：ufFull 不连底部，防止回流)
        if (row == size - 1) {
            uf.union(currentID, bottomNode);
        }

        // 4. 尝试连接上下左右的邻居
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
        validate(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        // 使用 ufFull 检查是否连通顶部，避免底部回流产生的错误 True
        return ufFull.find(topNode) == ufFull.find(xyTo1D(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSiteCount;
    }

    // does the system percolate?
    public boolean percolates() {
        // 只有这里使用带底部的 uf
        // 如果只有一个格子 (N=1)，需要特殊处理，或者依赖 open() 里的逻辑
        if (size == 1) {
            return isOpen(0, 0);
        }
        return uf.find(topNode) == uf.find(bottomNode);
    }

    // 辅助方法：将 2D 坐标转为 1D 索引
    private int xyTo1D(int row, int col) {
        return row * size + col;
    }

    // 辅助方法：检查边界
    private void validate(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    public static void main(String[] args) {
        // 单元测试代码 (可选)
    }
}