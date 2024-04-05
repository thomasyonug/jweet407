package ReadWriteLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class SharedResource {
    static int data = 0;
    static ReentrantReadWriteLock readWritelock = new ReentrantReadWriteLock();
}

class Producer extends Thread {
    @Override
    public void run() {
        Lock writeLock = SharedResource.readWritelock.writeLock();
        while(true){
            writeLock.lock();
            try {
                if(SharedResource.data < 100){
                    SharedResource.data +=1;
                    System.out.println("Producer writes: " + SharedResource.data);
                }else{
                    break;
                }

            } finally {
                writeLock.unlock();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

class Consumer extends Thread {
    @Override
    public void run() {
        Lock readLock = SharedResource.readWritelock.readLock();
        while (true) {
            readLock.lock();
            try {
                if(SharedResource.data < 100){
                    System.out.println("Consumer reads: " + SharedResource.data);

                }else {
                    break;
                }

            } finally {
                readLock.unlock();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

public class ReadWriteLockTest {
    public static void main(String[] args) {

        Producer producer1 = new Producer();
        Producer producer2 = new Producer();
        Consumer consumer1 = new Consumer();
        Consumer consumer2 = new Consumer();
        Consumer consumer3 = new Consumer();

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
        consumer3.start();
    }

}