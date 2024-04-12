/*
一个简单的银行账户系统，有两个用户（线程），一个用户存钱，另一个用户取钱。银行账户的余额是共享资源
 */
public class BankAccountExample {
    public static void main(String[] args) {
        DepositThread depositThread = new DepositThread();
        WithdrawThread withdrawThread = new WithdrawThread();
        depositThread.start();
        withdrawThread.start();
    }
}

class BankAccount {
    public static int balance;
    public static final Object lock = new Object();
}

class DepositThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            int amount = 100;
            synchronized (BankAccount.lock) {
                BankAccount.balance += amount;
                System.out.println("Deposited: " + amount + ", Current Balance: " + BankAccount.balance);
                BankAccount.lock.notifyAll();
            }
            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class WithdrawThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            int amount = 50;
            synchronized (BankAccount.lock) {
				if (BankAccount.balance < amount) {
                    System.out.println("Insufficient Balance, waiting for deposit...");
                    try {
                        BankAccount.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
				}
				BankAccount.balance -= amount;
				System.out.println("Withdrawn: " + amount + ", Current Balance: " + BankAccount.balance);
            }
        }
    }
}
