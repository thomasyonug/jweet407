class mData{
    public static int m = 1;
}
public class syncClass implements Runnable {

    @Override
    public void run() {
        addnum();
    }

    public static synchronized void addnum() {
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
        Runnable Add1 = new syncClass();
        Runnable Add2 = new syncClass();
        Thread t1 = new Thread(Add1);
        Thread t2 = new Thread(Add2);
        t1.start();
        t2.start();
    }
}
