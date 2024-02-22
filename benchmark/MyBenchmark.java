

package org.sample;


import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class MyBenchmark {
    static int[] array = new int[100_000_000];

    static {
        Arrays.fill(array, 1);
    }

    public int testMethod() throws ExecutionException, InterruptedException {
        // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
        // Put your benchmark code here.
        FutureTask<Integer> task1 = new FutureTask<>(() -> {
            int sum = 0;
            for (int i = 0; i < 25_000_000; i++) {
                sum += array[i];
            }
            return sum;
        });
        FutureTask<Integer> task2 = new FutureTask<>(() -> {
            int sum = 0;
            for (int i = 25_000_000; i < 50_000_000; i++) {
                sum += array[i];
            }
            return sum;
        });
        FutureTask<Integer> task3 = new FutureTask<>(() -> {
            int sum = 0;
            for (int i = 50_000_000; i < 75_000_000; i++) {
                sum += array[i];
            }
            return sum;
        });
        FutureTask<Integer> task4 = new FutureTask<>(() -> {
            int sum = 0;
            for (int i = 75_000_000; i < 100_000_000; i++) {
                sum += array[i];
            }
            return sum;
        });

        new Thread(task1).start();
        new Thread(task2).start();
        new Thread(task3).start();
        new Thread(task4).start();

        return task1.get() + task2.get() + task3.get() + task4.get();
    }


    public int noThreads() {
        int sum = 0;
        for (int i = 0; i < 100_000_000; i++) {
            sum += array[i];
        }
        return sum;
    }
}
