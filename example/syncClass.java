class comData{
    public static int m = 3;
    public static int n = 2;
}
public class syncClass implements Runnable {

    @Override
    public void run() {
        compare(comData.m,comData.n);
    }

    public  static synchronized int compare(int a,int b) {
        if(a > b){
            System.out.println("a>b");
            return 0;
        }
        else if(a == b){
            System.out.println("a=b");
            return 1;
        }
        else{
            System.out.println("a<b");
            return 2;
        }           
    }

    public static void main(String[] args) {
        Runnable Com = new syncFunc();
        Thread t1 = new Thread(Com);
        Thread t2 = new Thread(Com);
        t1.start();
        comData.n = comData.n + 2;
        t2.start();
    }
}

