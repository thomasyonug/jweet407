import java.util.LinkedList;
import java.util.Queue;

class SharedQueue {
    private Queue<Integer> queue = new LinkedList<>();
    private final int LIMIT = 10; // Maximum items in the queue
    private final Object lock = new Object();

    // Method for producers to add items to the queue
    public void produce(int item) throws InterruptedException {
        synchronized(lock) {
            while(queue.size() == LIMIT) { // Wait if the queue is full
                lock.wait();
            }
            queue.add(item);
            System.out.println("Produced: " + item);
            lock.notifyAll(); // Notify consumers that there is data to consume
        }
    }

    // Method for consumers to consume items from the queue
    public void consume() throws InterruptedException {
        synchronized(lock) {
            while(queue.isEmpty()) { // Wait if the queue is empty
                lock.wait();
            }
            int item = queue.remove();
            System.out.println("Consumed: " + item);
            lock.notifyAll(); // Notify producers that there is space to produce
        }
    }
}

class Producer extends Thread {
    private SharedQueue sharedQueue;

    public Producer(SharedQueue sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        for(int i = 0; i < 50; i++) {
            try {
                sharedQueue.produce(i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Consumer extends Thread {
    private SharedQueue sharedQueue;

    public Consumer(SharedQueue sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        for(int i = 0; i < 50; i++) {
            try {
                sharedQueue.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class SharedQueueExample {
    public static void main(String[] args) {
        SharedQueue sharedQueue = new SharedQueue();
        Producer producer = new Producer(sharedQueue);
        Consumer consumer = new Consumer(sharedQueue);

        producer.start();
        consumer.start();
    }
}