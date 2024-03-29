/*
 * 顾客去餐厅吃饭，厨师做饭，顾客吃完10份饭后停止
 * Cook 厨师 做完饭后放到桌子上，桌子上有饭时被阻塞
 * Customer 顾客 从桌子上取饭吃，桌子上没饭被阻塞
 * Desk 桌子，能存放一份饭
 *
 */

public class CookAndCustomer {
    public static void main(String[] args) {
        Cook cook = new Cook();
        Customer customer = new Customer();
        cook.start();
        customer.start();
    }
}

class Desk {
    public static int food_flag; //food_flag 标识桌上是否有食物，1为有，0为无
    public static int count = 10; //顾客还需吃的饭数
    public static Object lock = new Object(); //锁

}


class Cook extends Thread {
    @Override
    public void run() {
        while(true){
            synchronized (Desk.lock){
                if(Desk.count==0){
                    break;
                }else{
                    if(Desk.food_flag==1){
                        try {
                            Desk.lock.wait(); //桌上有食物，厨师被阻塞
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }else{
                        System.out.println("厨师做饭");
                        Desk.food_flag=1;
                    }
                    Desk.lock.notifyAll(); //唤醒
                }
            }
        }

    }
}
class Customer extends Thread{
    @Override
    public void run() {
        while(true){
            synchronized (Desk.lock){
                if(Desk.count==0){
                    break;
                }else{
                    if(Desk.food_flag==0){
                        try {
                            Desk.lock.wait();   //桌上无食物，顾客被阻塞
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }else{
                        System.out.println("客人吃饭");
                        Desk.count--;
                        System.out.println("还要吃"+Desk.count+"碗");
                        Desk.food_flag=0;
                    }
                    Desk.lock.notifyAll(); //唤醒厨师
                }
            }
        }
    }
}
