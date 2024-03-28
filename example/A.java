

class F {
    public int i;
}

public class A {
    public static void main(String[] args) throws InterruptedException {
        T t1 = new T();
        T t2 = new T();
        F f = new F();
        t1.f = f;
        t2.f = f;
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(f.i);

    }
}



class T extends Thread {
    public F f;
    public int j;
    public void run() {
        while (j < 5) {
            synchronized(f) {
                f.i++;
                System.out.println(f.i);
            }
            j++;
        }
    }
}

