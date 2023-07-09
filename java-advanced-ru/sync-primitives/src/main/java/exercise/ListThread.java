package exercise;

import java.util.Random;

// BEGIN
public class ListThread extends Thread {

    private final SafetyList safetyList;
    private final Random random = new Random();

    public ListThread(SafetyList safetyList) {
        this.safetyList = safetyList;
    }

    @Override
    public void run() {
        int amount = 0;
        while (amount < 1000) {
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
