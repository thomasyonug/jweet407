/*
一个简单的生产者-消费者问题，生产者生产数据，消费者消费数据，他们共享一个队列作为缓冲区。
 */
public class ProducerConsumerExample {
    public static void main(String[] args) {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        producer.start();
        consumer.start();
    }
}

class Buffer {
    public static final int CAPACITY = 5;
    public static int[] buffer = new int[CAPACITY];
    public static int count = 0;
    public static Object lock = new Object();
}

class Producer extends Thread {
    @Override
    public void run() {
        while (true) {
            synchronized (Buffer.lock) {
                while (Buffer.count == Buffer.CAPACITY) {
                    try {
                        Buffer.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Buffer.buffer[Buffer.count++] = 1;
                System.out.println("Produced: " + Buffer.count);
                Buffer.lock.notifyAll();
            }
        }
    }
}

class Consumer extends Thread {
    @Override
    public void run() {
        while (true) {
            synchronized (Buffer.lock) {
                while (Buffer.count == 0) {
                    try {
                        Buffer.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Buffer.count--;
                System.out.println("Consumed: " + (Buffer.count + 1));
                Buffer.lock.notifyAll();
            }
        }
    }
}
