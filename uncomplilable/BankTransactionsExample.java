class BankAccount {
    private int balance;
    private final Object lock = new Object();

    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(int amount) {
        synchronized (lock) {
            balance += amount;
            System.out.println("Deposited " + amount + ", New Balance: " + balance);
            lock.notifyAll();
        }
    }

    public void withdraw(int amount) {
        synchronized (lock) {
            while(balance < amount) {
                System.out.println("Waiting for deposit...");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            balance -= amount;
            System.out.println("Withdrew " + amount + ", New Balance: " + balance);
        }
    }

    public int getBalance() {
        return balance;
    }
}

class scope {
    public static BankAccount account;
    public static int amount;
}

class DepositThread extends Thread {
    public void run() {
        account.deposit(amount);
    }
}

class WithdrawThread extends Thread {
    private BankAccount account;
    private int amount;

    public WithdrawThread(BankAccount account, int amount) {
        this.account = account;
        this.amount = amount;
    }

    public void run() {
        account.withdraw(amount);
    }
}

public class BankTransactionsExample {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(100);

        for (int i = 0; i < 5; i++) {

            new WithdrawThread().start(); 
            new DepositThread(account, 150).start(); 
        }
    }
}
