package exercise;

import java.util.Arrays;

public class MaxThread extends Thread {

    private final int[] numbers;
    private int max;

    public MaxThread(int[] numbers) {
        this.numbers = numbers;
    }

    public int getMax() {
        return Arrays.stream(numbers).max().getAsInt();
    }

    @Override
    public void run() {
        super.run();
    }
}
