
public class CookAndCustomer {
    public static void main(String[] args) {
        Cook cook = new Cook();
        Customer customer = new Customer();
        cook.start();
        customer.start();
    }
}

class Desk {
    public static int food_flag;
    public static int count = 10;
    public static Object lock = new Object();
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
                            Desk.lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }else{
                        System.out.println("cook");
                        Desk.food_flag=1;
                    }
                    Desk.lock.notifyAll();
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
                            Desk.lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }else{
                        System.out.println("eat");
                        Desk.count--;
                        System.out.println("want to eat "+Desk.count+" more times");
                        Desk.food_flag=0;
                    }
                    Desk.lock.notifyAll();
                }
            }
        }
    }
}
