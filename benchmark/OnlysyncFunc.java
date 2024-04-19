class nData{
    public static int n = 1;
}
public class OnlysyncFunc implements Runnable {

    @Override
    public void run() {
        addnum();
    }

    public synchronized void addnum() {
        int i = 0;
        System.out.println("begin");
        while(i != 10000){
            nData.n = nData.n + 1;
            i = i + 1;
        }
        System.out.println(nData.n);      
        System.out.println("end");    
    }

    public static void main(String[] args) {
        Runnable Add1 = new OnlysyncFunc();
        Runnable Add2 = new OnlysyncFunc();
        Thread t1 = new Thread(Add1);
        Thread t2 = new Thread(Add2);
        t1.start();
        t2.start();
    }
}