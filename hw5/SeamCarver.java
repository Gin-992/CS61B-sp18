import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture p;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture is null");
        }

        this.p = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(p);
    }

    // width of current picture
    public int width() {
        return p.width();
    }

    // height of current picture
    public int height() {
        return p.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }

        int xLeft = (x == 0) ? width() - 1 : x - 1;
        int xRight = (x == width() - 1) ? 0 : x + 1;

        int yUp = (y == 0) ? height() - 1 : y - 1;
        int yDown = (y == height() - 1) ? 0 : y + 1;

        Color left = picture().get(xLeft, y);
        Color right = picture().get(xRight, y);
        Color up = picture().get(x, yUp);
        Color down = picture().get(x, yDown);

        // 幂函数：power function
        double dx2 = Math.pow(left.getRed() - right.getRed(), 2)
                + Math.pow(left.getGreen() - right.getGreen(), 2)
                + Math.pow(left.getBlue() - right.getBlue(), 2);

        double dy2 = Math.pow(up.getRed() - down.getRed(), 2)
                + Math.pow(up.getGreen() - down.getGreen(), 2)
                + Math.pow(up.getBlue() - down.getBlue(), 2);

        return dx2 + dy2;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture raw = picture();
        Picture tran = new Picture(raw.height(), raw.width());

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                tran.set(j, i, raw.get(i, j));
            }
        }

        this.p = tran;
        int[] re = findVerticalSeam();
        this.p = raw;
        return re;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];

        for (int i = 0; i < width(); i++) {
            distTo[i][0] = energy(i, 0);
        }

        for (int i = 1; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                int prevCol = min(j, i, distTo);
                distTo[j][i] = distTo[prevCol][i - 1] + energy(j, i);
                edgeTo[j][i] = prevCol;
            }
        }

        double minEnergy = Double.MAX_VALUE;
        int minCol = 0;
        for (int i = 0; i < width(); i++) {
            if (distTo[i][height() - 1] < minEnergy) {
                minEnergy = distTo[i][height() - 1];
                minCol = i;
            }
        }

        int[] path = new int[height()];
        path[height() - 1] = minCol;
        for (int i = 1; i < height(); i++) {
            path[height() - 1 - i] = edgeTo[path[height() - i]][height() - i];
        }

        return path;
    }

    private int min(int x, int y, double[][] distTo) {
        int bestCol = x;

        if (x > 0) {
            if (distTo[x - 1][y - 1] < distTo[bestCol][y - 1]) {
                bestCol = x - 1;
            }
        }

        if (x < width() - 1) {
            if (distTo[x + 1][y - 1] < distTo[bestCol][y - 1]) {
                bestCol = x + 1;
            }
        }

        return bestCol;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width() || height() <= 1) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("Seam is not valid: jumps too far.");
            }
            if (seam[i] < 0 || seam[i] >= height()) {
                throw new IllegalArgumentException("Seam index out of bounds.");
            }
        }
        if (seam[seam.length - 1] < 0 || seam[seam.length - 1] >= height()) {
            throw new IllegalArgumentException();
        }

        this.p = SeamRemover.removeHorizontalSeam(p, seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height() || width() <= 1) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("Seam is not valid: jumps too far.");
            }
            if (seam[i] < 0 || seam[i] >= width()) {
                throw new IllegalArgumentException("Seam index out of bounds.");
            }
        }
        if (seam[seam.length - 1] < 0 || seam[seam.length - 1] >= width()) {
            throw new IllegalArgumentException();
        }

        this.p = SeamRemover.removeVerticalSeam(p, seam);
    }
}
