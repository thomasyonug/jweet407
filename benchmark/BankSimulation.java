/*
*用户去银行办理业务
* ServiceWindow 服务窗口 没有等待的用户时被阻塞
* Customer 用户 没有空闲窗口时被阻塞
 */
public class BankSimulation {
    public static void main(String[] args) {
        ServiceWindow serviceWindow = new ServiceWindow();
        Customer customer = new Customer();
        serviceWindow.start();
        customer.start();
    }
}

class Bank {
    public static int windowWorkNum = 5; // 窗口工作次数，用于退出循环
    public static int freeWindowFlag = 1;//表示是否有空闲窗口，1为有空闲窗口，0为无空闲窗口
    public static int waitCustomerFlag = 0;//表示是否有等待客户，1为有等待客户，0为无等待客户
    public static Object lock = new Object();
}

class ServiceWindow extends Thread {
    @Override
    public void run() {
        while (true) {
            synchronized (Bank.lock) {
                if (Bank.windowWorkNum == 0) {
                    break;
                } else {
                    if (Bank.waitCustomerFlag == 0) {
                        try {
                            Bank.lock.wait(); // 没有等待客户，阻塞自身
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("窗口处理用户业务，还需处理" + Bank.windowWorkNum + "次业务");
                        Bank.waitCustomerFlag = 0;  //等待用户已被处理
                        Bank.freeWindowFlag = 1; //处理完成，窗口空闲
                        Bank.windowWorkNum--;
                    }
                    Bank.lock.notifyAll();
                }
            }
        }
    }
}

class Customer extends Thread {
    @Override
    public void run() {
        while (true) {
            synchronized (Bank.lock) {
                if (Bank.windowWorkNum == 0) {
                    break;
                } else {

                    if (Bank.freeWindowFlag == 0) {
                        try {

                            Bank.lock.wait(); // 没有空闲窗口，阻塞自身

                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("客户请求处理");
                        Bank.freeWindowFlag = 0;  //请求服务窗口
                        Bank.waitCustomerFlag = 1;  //用户等待

                    }
                    Bank.lock.notifyAll();
                }
            }
        }
    }
}
