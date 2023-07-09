package exercise;

class App {

    public static void main(String[] args) {
        // BEGIN
        var safetyList = new SafetyList();
        var firstThread = new ListThread(safetyList);
        var secondThread = new ListThread(safetyList);

        firstThread.start();
        secondThread.start();

        try {
            firstThread.join();
            secondThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(safetyList.getSize());
        // END
    }
}

