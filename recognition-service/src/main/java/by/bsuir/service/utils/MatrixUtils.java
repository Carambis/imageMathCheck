package by.bsuir.service.utils;

public class MatrixUtils {
    public static int[][] deleteZeroLine(int[][] image) {
        while (checkRow(image, 0)) {
            deleteFirstRow(image);
        }
        while (checkRow(image, image.length - 1)) {
            image = deleteLastRow(image);
        }
        while (checkColumn(image, 0)) {
            deleteLeftColumn(image);
        }
        while (checkColumn(image, image[0].length - 1)) {
            image = deleteRightColumn(image);
        }
        return image;
    }

    private static boolean checkRow(int[][] image, int row) {
        int width = image[row].length;
        for (int i = 0; i < width; i++) {
            if (image[row][i] == 1) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkColumn(int[][] image, int column) {
        for (int[] anImage : image) {
            if (anImage[column] == 1) {
                return false;
            }
        }
        return true;
    }

    private static void deleteFirstRow(int[][] image) {
        if (image.length - 1 >= 0) {
            System.arraycopy(image, 1, image, 0, image.length - 1);
        }
    }

    private static int[][] deleteLastRow(int[][] image) {
        int[][] newImage = new int[image.length - 1][image[0].length];
        System.arraycopy(image, 0, newImage, 0, newImage.length);
        return newImage;
    }

    private static void deleteLeftColumn(int[][] image) {
        for (int[] anImage : image) {
            if (image[0].length - 1 >= 0) {
                System.arraycopy(anImage, 1, anImage, 0, image[0].length - 1);
            }
        }
    }

    private static int[][] deleteRightColumn(int[][] image) {
        int[][] newImage = new int[image.length][image[0].length - 1];
        for (int i = 0; i < newImage.length; i++) {
            if (newImage[0].length - 1 >= 0) {
                System.arraycopy(image[i], 0, newImage[i], 0, newImage[0].length);
            }
        }
        return newImage;
    }
}
