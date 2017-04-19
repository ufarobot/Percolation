import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] probes;

    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("n must be greater than zero");
        if (trials <= 0) throw new IllegalArgumentException("trials must be greater than zero");
        probes = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                }
            }
            probes[i] = (double) perc.numberOfOpenSites() / (n * n);
        }
    }

    public static void main(String[] args) {
        int n = 2;
        int trials = 10000000;
        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println(stats.mean());
        System.out.println(stats.stddev());
        System.out.println(stats.confidenceLo());
        System.out.println(stats.confidenceHi());
    }

    public double mean() {
        return StdStats.mean(probes);
    }

    public double stddev() {
        return StdStats.stddev(probes);
    }

    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(probes.length);
    }

    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(probes.length);
    }
}
