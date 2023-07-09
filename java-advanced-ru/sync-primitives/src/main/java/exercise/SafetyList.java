package exercise;

import java.util.ArrayList;
import java.util.List;

class SafetyList {
    // BEGIN
    private volatile List<Integer> values = new ArrayList<>();
    // END

    public void add(Integer value) {
        synchronized (this) {
            values.add(value);
        }
    }

    public int get(int index) {
        return values.get(index);
    }

    public int getSize() {
        return values.size();
    }
}
