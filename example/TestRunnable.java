

/**
 * 多线程创建：实现Runnable接口
 *
 * @author mikechen
 */
public class MyRunnable implements Runnable {
    private int i = 0;

    @Override
    public void run() {
        for (i = 0; i < 10; i++) {
            System.out.println("Runnable " + i);
        }
    }


    public static void main(String[] args) {
        Runnable myRunnable = new MyRunnable(); // 创建一个Runnable实现类的对象
        Thread thread = new Thread(myRunnable); // 将myRunnable作为Thread target创建新的线程
        thread.start();
    }
}