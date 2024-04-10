class Data {
    public volatile static  boolean  stop = false;
}

public class VolatileTest{
    public static void main(String args[]) throws InterruptedException {
        TestThread testThread = new TestThread();
        testThread.start();
        Thread.sleep(100);
        TestThread2 testThread2 = new TestThread2();
        testThread2.start();
        System.out.println("now, in main thread stop is: " + Data.stop);
        testThread.join();
        testThread2.join();
    }
}

class TestThread extends Thread {
    @Override
    public void run() {
        int i = 1;
        while (!Data.stop) {
            i++;
        }
        System.out.println("Thread1 stop: i=" + i);
    }
}

class TestThread2 extends Thread {
    @Override
    public void run() {
        Data.stop = true;
        System.out.println("Thread2 update stop to: " + Data.stop);
    }
}
