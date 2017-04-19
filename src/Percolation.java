import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int sourceSite;
    private int destSite;
    private int n;
    private WeightedQuickUnionUF connectedSitesUF;
    private WeightedQuickUnionUF filledSitesUF;     // to solve "backwash" problem
    private boolean[][] isOpen;
    private int numberOfOpenSites;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be greater than zero");
        this.n = n;
        isOpen = new boolean[n + 1][n + 1];
        connectedSitesUF = new WeightedQuickUnionUF(n * n + 2);
        filledSitesUF = new WeightedQuickUnionUF(n * n + 1);
        sourceSite = n * n;
        destSite = n * n + 1;

        // connecting top sites to source and bottom sites to dest
        for (int i = 0; i < n; i++) {
            connectedSitesUF.union(sourceSite, i);
            connectedSitesUF.union(destSite, (n - 1) * n + i);

            // connecting filled sites to source, but not to dest to solve "backwash" problem
            filledSitesUF.union(sourceSite, i);
        }
    }

    public void open(int row, int col) {
        validateIndicies(row, col);

        if (!isOpen[row][col]) {
            isOpen[row][col] = true;
            numberOfOpenSites++;
        }

        // try to connect neighbor sites
        if (row - 1 > 0 && isOpen[row - 1][col]) {
            connectedSitesUF.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            filledSitesUF.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        }
        if (row + 1 <= n && isOpen[row + 1][col]) {
            connectedSitesUF.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            filledSitesUF.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }
        if (col - 1 > 0 && isOpen[row][col - 1]) {
            connectedSitesUF.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            filledSitesUF.union(xyTo1D(row, col), xyTo1D(row, col - 1));
        }
        if (col + 1 <= n && isOpen[row][col + 1]) {
            connectedSitesUF.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            filledSitesUF.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }

    }

    public boolean isOpen(int row, int col) {
        validateIndicies(row, col);
        return isOpen[row][col];
    }

    public boolean isFull(int row, int col) {
        validateIndicies(row, col);
        return isOpen[row][col] && filledSitesUF.connected(xyTo1D(row, col), sourceSite);
    }

    private void validateIndicies(int row, int col) {
        if (row <= 0 || row > n) throw new IndexOutOfBoundsException("row index out of bounds");
        if (col <= 0 || col > n) throw new IndexOutOfBoundsException("col index out of bounds");
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        if (n == 1) return isOpen(1, 1);
        return connectedSitesUF.connected(sourceSite, destSite);
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * n + (col - 1);
    }
}
