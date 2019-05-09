package by.bsuir.service.utils;

public class CustomNeuralNetwork {
    private static final int BIAS = 13;

    public static boolean proceed(int[] number, int[] weights) {
        int net = 0;
        for (int i = 0; i < number.length; i++) {
            net += number[i] * weights[i];
        }
        return net >= BIAS;
    }

    public static void decrease(int[] number, int[] weights) {
        for (int i = 0; i < number.length; i++) {
            if (number[i] == 1) {
                weights[i]--;
            }
        }
    }

    public static void increase(int[] number, int[] weights) {
        for (int i = 0; i < number.length; i++) {
            if (number[i] == 1) {
                weights[i]++;
            }
        }
    }

}
