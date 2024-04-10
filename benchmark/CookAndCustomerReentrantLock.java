

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CookAndCustomerReentrantLock {
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
    public static ReentrantLock reentrantLock = new ReentrantLock();
    public static Condition condition = reentrantLock.newCondition();
}
class Cook extends Thread{
    @Override
    public void run() {
        while(true){
            Desk.reentrantLock.lock();
            try{
                if(Desk.count==0){
                    break;
                }else{
                    if(Desk.food_flag==1){
                        try {
                            Desk.condition.await(); //桌上有食物，厨师被阻塞
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        System.out.println("厨师做饭");
                        Desk.food_flag=1;
                        Desk.condition.signalAll(); //唤醒
                    }
                }
            }finally {
                Desk.reentrantLock.unlock();
            }
        }
    }
}
class Customer extends Thread{
    @Override
    public void run() {
        while(true){
            Desk.reentrantLock.lock();
            try{
                if(Desk.count==0){
                    break;
                }else{
                    if(Desk.food_flag==0){
                        try {
                            Desk.condition.await();   //桌上无食物，顾客被阻塞
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }else{
                        System.out.println("客人吃饭");
                        Desk.count--;
                        System.out.println("还要吃"+Desk.count+"碗");
                        Desk.food_flag=0;
                        Desk.condition.signalAll();
                    }

                }
            }finally {
                Desk.reentrantLock.unlock();
            }
        }

    }
}