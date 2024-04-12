class mData{
    public static int m = 1;
}
public class syncFunc implements Runnable {

    @Override
    public void run() {
        addnum();
    }

    public synchronized void addnum() {
        int i = 0;
        System.out.println("begin");
        while(i != 10000){
            mData.m = mData.m + 1;
            i = i + 1;
        }
        System.out.println(mData.m);      
        System.out.println("end");    
    }

    public static void main(String[] args) {
        Runnable Add = new syncFunc();
        Thread t1 = new Thread(Add);
        Thread t2 = new Thread(Add);
        t1.start();
        t2.start();
    }
}

