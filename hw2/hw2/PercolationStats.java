package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private final double[] fractions; // 存储每次实验的阈值结果
    private final int t;              // 实验次数

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T must be greater than 0");
        }

        this.t = T;
        this.fractions = new double[T];

        for (int i = 0; i < T; i++) {
            // 1. 使用工厂创建 Percolation 对象
            Percolation sys = pf.make(N);

            // 2. 持续打开格子直到系统连通
            while (!sys.percolates()) {
                int row, col;
                // 随机选择一个 blocked (未打开) 的格子
                // 注意：如果之前的 Percolation 是 0-based 索引 (0 到 N-1)，这里正好匹配
                do {
                    row = StdRandom.uniform(N);
                    col = StdRandom.uniform(N);
                } while (sys.isOpen(row, col));

                sys.open(row, col);
            }

            // 3. 记录当前的开通比例
            double fraction = (double) sys.numberOfOpenSites() / (N * N);
            fractions[i] = fraction;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        double mu = mean();
        double sigma = stddev();
        return mu - (1.96 * sigma / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        double mu = mean();
        double sigma = stddev();
        return mu + (1.96 * sigma / Math.sqrt(t));
    }
}