package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF uf;      // 用于判断系统是否渗透
    private WeightedQuickUnionUF ufFull;  // 用于判断格子是否满水 (防止回流)
    private int size;
    private int topNode;
    private int bottomNode;
    private int openSiteCount;

    // 构造函数：N <= 0 时抛出 IllegalArgumentException
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be > 0");
        }
        this.size = N;
        this.openSiteCount = 0;
        this.grid = new boolean[N][N];

        // 索引 0 到 N^2-1 是格子
        int totalNodes = N * N;
        this.topNode = totalNodes;
        this.bottomNode = totalNodes + 1;

        this.uf = new WeightedQuickUnionUF(totalNodes + 2);     // 带头带尾
        this.ufFull = new WeightedQuickUnionUF(totalNodes + 1); // 只带头
    }

    // open 方法：越界时必须抛出 IndexOutOfBoundsException
    public void open(int row, int col) {
        validate(row, col); // 1. 必须先检查异常

        if (grid[row][col]) { // 直接读数组，因为已经 validate 过了
            return;
        }

        // 2. 打开格子
        grid[row][col] = true;
        openSiteCount++;

        int currentID = xyTo1D(row, col);

        // 3. 连通虚拟顶部
        if (row == 0) {
            uf.union(currentID, topNode);
            ufFull.union(currentID, topNode);
        }

        // 4. 连通虚拟底部 (仅 uf)
        if (row == size - 1) {
            uf.union(currentID, bottomNode);
        }

        // 5. 连通四周邻居 (需要复用 validate 逻辑或者手动检查边界)
        // 上
        if (row > 0 && grid[row - 1][col]) {
            int upID = xyTo1D(row - 1, col);
            uf.union(currentID, upID);
            ufFull.union(currentID, upID);
        }
        // 下
        if (row < size - 1 && grid[row + 1][col]) {
            int downID = xyTo1D(row + 1, col);
            uf.union(currentID, downID);
            ufFull.union(currentID, downID);
        }
        // 左
        if (col > 0 && grid[row][col - 1]) {
            int leftID = xyTo1D(row, col - 1);
            uf.union(currentID, leftID);
            ufFull.union(currentID, leftID);
        }
        // 右
        if (col < size - 1 && grid[row][col + 1]) {
            int rightID = xyTo1D(row, col + 1);
            uf.union(currentID, rightID);
            ufFull.union(currentID, rightID);
        }
    }

    // isOpen 方法：越界时必须抛出 IndexOutOfBoundsException
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row][col];
    }

    // isFull 方法：越界时必须抛出 IndexOutOfBoundsException
    public boolean isFull(int row, int col) {
        validate(row, col);
        if (!grid[row][col]) {
            return false;
        }
        // 使用 ufFull 防止回流
        return ufFull.connected(topNode, xyTo1D(row, col));
    }

    public int numberOfOpenSites() {
        return openSiteCount;
    }

    public boolean percolates() {
        if (size == 1) {
            return grid[0][0];
        }
        return uf.connected(topNode, bottomNode);
    }

    // 辅助方法：统一处理异常抛出
    private void validate(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException("Index " + row + ", " + col + " is out of bounds");
        }
    }

    private int xyTo1D(int row, int col) {
        return row * size + col;
    }

    public static void main(String[] args) {
        // test
    }
}