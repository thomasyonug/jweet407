class Data {
    public volatile static  boolean  stop = false;
    public static Thread cntThread;
}

public class VolatileTest{
    public static void main(String args[]){
        TestThread testThread = new TestThread();
        testThread.start();
        TestThread2 testThread2 = new TestThread2();
        testThread2.start();
        System.out.println("now, in main thread stop is: " + Data.stop);
    }
}

class TestThread extends Thread {
    @Override
    public void run() {
        Data.cntThread = this;
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Data.stop = true;
        System.out.println("Thread2 update stop to: " + Data.stop);
        if (Data.cntThread!= null) {
            System.out.println("Thread2 waiting for Thread1 to stop");
            try {
                Data.cntThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
