public class ArraySum {
    private int[] array;
    private final Object lock = new Object();
    private int totalSum = 0;

    public ArraySum(int[] array) {
        this.array = array;
    }

    private class SumThread extends Thread {
        private int start, end;

        public SumThread(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }

            synchronized (lock) {
                totalSum += sum;
            }
        }
    }

    public int calculateSum() throws InterruptedException {
        SumThread t1 = new SumThread(0, array.length / 4);
        SumThread t2 = new SumThread(array.length / 4, array.length / 2);
        SumThread t3 = new SumThread(array.length / 2, 3 * array.length / 4);
        SumThread t4 = new SumThread(3 * array.length / 4, array.length);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        return totalSum;
    }

    public static void main(String[] args) throws InterruptedException {
        // Example usage
        int size = 100_000_000;
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = 1; // simplification for demonstration
        }

        ArraySum calculator = new ArraySum(array);
        int sum = calculator.calculateSum();
        System.out.println("Total sum: " + sum);
    }
}
