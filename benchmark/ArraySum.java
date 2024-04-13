class SumThread extends Thread {
	@Override
	public void run() {
		int sum = 0;
		for (int i = 0; i < 1000; i++) {
			sum += ConcurrentArray.array[i];
		}

		synchronized (ConcurrentArray.lock) {
			ConcurrentArray.totalSum += sum;
		}
	}
}

class ConcurrentArray {
    public static int[] array = new int[1000];
	static {
        for (int i = 0; i < 1000; i++) {
            array[i] = 1;
        }
	}
    public static final Object lock = new Object();
    public static int totalSum = 0;
}

class ArraySum {

    public static void calculateSum() throws InterruptedException {
        SumThread t1 = new SumThread();
        SumThread t2 = new SumThread();
        SumThread t3 = new SumThread();
        SumThread t4 = new SumThread();

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    public static void main(String[] args) throws InterruptedException {
        // Example usage
        calculateSum();
    }
}
