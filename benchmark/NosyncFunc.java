class nData {
    public static int n = 1;
}
public class NosyncFunc implements Runnable {

    @Override
    public void run() {
        addnum();
    }

    public void addnum() {
        int i = 0;
        System.out.println("begin");
        while(i != 1000) {
            nData.n = nData.n + 1;
            i = i + 1;
        }
        System.out.println(nData.n);
        System.out.println("end");
    }

    public static void main(String[] args) {
        Runnable Add = new NosyncFunc();
        Thread t1 = new Thread(Add);
        Thread t2 = new Thread(Add);
        t1.start();
        t2.start();
    }
}

