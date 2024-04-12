/*
一个较为复杂的生产者-消费者问题。
问题描述：
桌子上有一个盘子，每次只能向其中放入一个水果。
爸爸专向盘子中放苹果，妈妈专向盘子中放橘子，儿子专等吃盘子中的橘子，女儿专等吃盘子中的苹果。
只有盘子为空时，爸爸或妈妈才可向盘子中放一个水果；
仅当盘子中有自己需要的水果时，儿子或女儿可以从盘子中取出。
 */
public class MultProducerConsumer {
    public static void main(String[] args) {
        Plate plate = new Plate();
        Dad dad = new Dad();
        Mom mom = new Mom();
        Son son = new Son();
        Daughter daughter = new Daughter();

        dad.start();
        mom.start();
        son.start();
        daughter.start();
    }
}

class Plate {
    public static String fruit;
    public static boolean available = false;
    public static static Object lock = new Object(); // 锁

}

class Dad extends Thread {
    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            synchronized (Plate.lock) {
                while (Plate.available) {
                    try {
                        Plate.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Plate.fruit = "Apple";
                Plate.available = true;
                System.out.println("放入了：Apple");
                Plate.lock.notifyAll();
            }
        }
    }
}

class Mom extends Thread {
    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            synchronized (Plate.lock) {
                while (Plate.available) {
                    try {
                        Plate.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Plate.fruit = "Orange";
                Plate.available = true;
                System.out.println("放入了：Orange" );
                Plate.lock.notifyAll();
            }
        }
    }
}

class Son extends Thread {
    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            synchronized (Plate.lock) {
                while (!Plate.available || !Plate.fruit.equals("Orange")) {
                    try {
                        Plate.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Plate.available = false;
                System.out.println("儿子吃了：Orange");
                Plate.lock.notifyAll();
            }
        }
    }
}
class Daughter extends Thread {
    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            synchronized ( Plate.lock) {
                while (!Plate.available || !Plate.fruit.equals("Apple")) {
                    try {
                        Plate.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Plate.available = false;
                System.out.println("女儿吃了：Apple" );
                Plate.lock.notifyAll();
            }
        }
    }
}
