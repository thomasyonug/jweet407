
class Scope {
    public static Thread t1;
    public static Object lock = new Object();
}


class Loop extends Thread {
    public void run() {
        synchronized (Scope.lock) {
            Scope.t1 = this;
            System.out.println("loop start run");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("loop wake from sleep");
        }
    }
}

public class Join extends Thread {
    public void run() {
        while (true) {
            synchronized (Scope.lock) {
                System.out.println("join start lock");
                if (Scope.t1 != null) {
                    System.out.println("join start run");
                    try {
                        Scope.t1.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("join after join");
                    return;
                }
            }
        }


    }

    public static void main(String[] args) {
        Thread t1 = new Loop();
        Thread t2 = new Join();
        t1.start();
        t2.start();
    }
}