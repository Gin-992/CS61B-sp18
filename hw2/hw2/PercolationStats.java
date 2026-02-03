package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] res;
    private int num;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Argument should larger than 0.");
        }

        res = new double[T];
        num = T;

        for (int i = 0; i < T; i++) {
            Percolation per = pf.make(N);

            while (!per.percolates()) {
                while (true) {
                    int row = StdRandom.uniform(N);
                    int col = StdRandom.uniform(N);
                    if (!per.isOpen(row, col)) {
                        per.open(row, col);
                        break;
                    }
                }
            }

            res[i] = (double) per.numberOfOpenSites() / (N * N);
        }
    }

    public double mean() {
        return StdStats.mean(res);
    }

    public double stddev() {
        return StdStats.stddev(res);
    }

    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(num);
    }

    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(num);
    }
}
