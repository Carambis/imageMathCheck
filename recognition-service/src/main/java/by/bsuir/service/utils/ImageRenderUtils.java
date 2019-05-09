package by.bsuir.service.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageRenderUtils {

    public static int[][] getBW(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        int[][] bwImage = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = image.getRGB(j, i);
                Color color = new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
                bwImage[i][j] = color.equals(Color.BLACK) ? 1 : 0;
            }
        }
        return bwImage;
    }

    public static BufferedImage scaleImage(int[][] image, int width, int height) {
        BufferedImage newScaledImage = toBufferedImage(image);

        BufferedImage scaled = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.drawImage(newScaledImage, 0, 0, width, height, null);
        g.dispose();

        return scaled;
    }

    public static BufferedImage toBufferedImage(int[][] image) {
        BufferedImage bi = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] == 1) {
                    bi.setRGB(j, i, 0);
                } else {
                    bi.setRGB(j, i, 1);
                }
            }
        }
        return bi;
    }

    public static int[] readImage(String fileName, int width, int height) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(fileName));
        int[][] image = ImageRenderUtils.getBW(bufferedImage);
        image = MatrixUtils.deleteZeroLine(image);
        BufferedImage scaled = scaleImage(image, width, height);
        image = ImageRenderUtils.getBW(scaled);
        return toSimpleArray(image);
    }

    public static int[] toSimpleArray(int[][] image){
        int[] points = new int[image.length * image[0].length];
        int k = 0;
        for (int[] array : image) {
            for (int anArray : array) {
                points[k] = anArray;
                k++;
            }
        }
        return points;
    }

    public static java.util.List<int[]> segment(int[][] image) {
        int height = image[0].length;
        int width = image.length;
        int maxLabels = 100;

        int[][] labels = new int[width][height];
        int[] equiv = new int[maxLabels];
        boolean[] used = new boolean[maxLabels];

        int[][] bbs = new int[maxLabels][4];
        List<int[]> result = new ArrayList<int[]>();
        for (int i = 0; i < maxLabels; i++) {
            equiv[i] = i;
            used[i] = false;
        }

        int currentLabel = 0;
        int[] neigh;
        int nbNei;
        int minLabel;
        for (int y = 1; y < height - 1; y++)
            for (int x = 1; x < width - 1; x++) {
                if (image[x][y] == 1) {
                    neigh = new int[4];
                    nbNei = 0;

                    if (labels[x - 1][y] > 0) neigh[nbNei++] = labels[x - 1][y];
                    if (labels[x - 1][y - 1] > 0) neigh[nbNei++] = labels[x - 1][y - 1];
                    if (labels[x][y - 1] > 0) neigh[nbNei++] = labels[x][y - 1];
                    if (labels[x + 1][y - 1] > 0) neigh[nbNei++] = labels[x + 1][y - 1];

                    if (nbNei == 0)
                        labels[x][y] = ++currentLabel;
                    else {
                        minLabel = maxLabels;
                        for (int i = 0; i < nbNei; i++)
                            if (neigh[i] < minLabel)
                                minLabel = equiv[neigh[i]];
                        labels[x][y] = equiv[minLabel];
                        for (int i = 0; i < nbNei; i++)
                            if (equiv[neigh[i]] > minLabel)
                                equiv[neigh[i]] = equiv[minLabel];
                    }
                }
            }
        for (int i = 0; i < maxLabels; i++)
            while (equiv[i] > equiv[equiv[i]]) equiv[i] = equiv[equiv[i]];

        int actLabel;
        for (int x = 1; x < width; x++)
            for (int y = 1; y < height - 1; y++) {
                if (labels[x][y] > 0) {
                    actLabel = equiv[labels[x][y]];
                    if (!used[actLabel]) {
                        bbs[actLabel][0] = x;
                        bbs[actLabel][1] = x;
                        bbs[actLabel][2] = y;
                        bbs[actLabel][3] = y;
                        used[actLabel] = true;
                    } else {
                        if (bbs[actLabel][0] > x) bbs[actLabel][0] = x;
                        if (bbs[actLabel][1] < x) bbs[actLabel][1] = x;
                        if (bbs[actLabel][2] > y) bbs[actLabel][2] = y;
                        if (bbs[actLabel][3] < y) bbs[actLabel][3] = y;
                    }
                }
            }

        for (int i = 0; i < maxLabels; i++)
            if (used[i]) {
                result.add(bbs[i]);
            }

        return result;

    }

}
