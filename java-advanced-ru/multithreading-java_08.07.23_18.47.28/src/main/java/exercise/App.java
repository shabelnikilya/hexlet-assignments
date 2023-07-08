package exercise;

import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

class App {
    private static final Logger LOGGER = Logger.getLogger("AppLogger");

    public static Map<String, Integer> getMinMax(int[] numbers) {
        MinThread min = new MinThread(numbers, LOGGER);
        MaxThread max = new MaxThread(numbers, LOGGER);

        min.start();
        max.start();

        try {
            min.join();
            max.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Map.of(
                "min", min.getMin(),
                "max", max.getMax()
        );
    }
}
