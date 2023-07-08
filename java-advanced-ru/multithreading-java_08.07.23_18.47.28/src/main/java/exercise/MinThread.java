package exercise;

import java.util.Arrays;
import java.util.logging.Logger;

// BEGIN
public class MinThread extends Thread {
    private final int[] numbers;
    private final Logger logger;
    private int min;

    public MinThread(int[] numbers, Logger logger) {
        this.numbers = numbers;
        this.logger = logger;
    }

    @Override
    public void run() {
        logger.info("Thread start (find min)");
        min = Arrays.stream(numbers).min().getAsInt();
        logger.info("Thread finish (find min)");
    }

    public int getMin() {
        return min;
    }
}
// END
