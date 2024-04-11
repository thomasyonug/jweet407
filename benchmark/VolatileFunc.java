class mData{
    public volatile static int m = 1;
}
public class VolatileFunc implements Runnable {

    @Override
    public void run() {
        addnum();
    }

    public void addnum() {
        int i = 0;
        System.out.println("begin");
        while(i != 1000){
            mData.m = mData.m + 1;
            i = i + 1;
        }
        System.out.println(mData.m);
        System.out.println("end");
    }

    public static void main(String[] args) {
        Runnable Add = new VolatileFunc();
        Thread t1 = new Thread(Add);
        Thread t2 = new Thread(Add);
        t1.start();
        t2.start();
    }
}

