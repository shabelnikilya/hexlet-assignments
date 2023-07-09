package exercise;

import java.util.Random;

// BEGIN
public class ListThread extends Thread {
    private final static int LIMIT = 1000;

    private final SafetyList safetyList;
    private final Random random = new Random();

    public ListThread(SafetyList safetyList) {
        this.safetyList = safetyList;
    }

    @Override
    public void run() {
        int amount = 0;
        while (amount < LIMIT) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            safetyList.add(random.nextInt());
            amount++;
        }
    }
}
// END
