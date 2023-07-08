package exercise;

import java.util.Arrays;
import java.util.logging.Logger;

public class MaxThread extends Thread {

    private final int[] numbers;
    private final Logger logger;
    private int max;

    public MaxThread(int[] numbers, Logger logger) {
        this.numbers = numbers;
        this.logger = logger;
    }

    public int getMax() {
        return Arrays.stream(numbers).max().getAsInt();
    }

    @Override
    public void run() {
        logger.info("Thread start (find max)");
        max = Arrays.stream(numbers).max().getAsInt();
        logger.info("Thread finish (find max)");
    }
}
