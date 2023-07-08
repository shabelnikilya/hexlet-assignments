package exercise;

import java.util.Arrays;

public class MinThread extends Thread {
    private final int[] numbers;
    private int min;

    public MinThread(int[] numbers) {
        this.numbers = numbers;
    }

    @Override
    public void run() {
        min = Arrays.stream(numbers).min().getAsInt();
    }

    public int getMin() {
        return min;
    }
}
