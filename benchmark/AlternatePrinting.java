/*
交替打印奇偶数
在这个案例中，我们有两个线程，一个线程负责打印奇数，另一个线程负责打印偶数，它们交替执行。
 */
public class AlternatePrinting {
    public static void main(String[] args) {
        OddNumberPrinter oddNumberPrinter = new OddNumberPrinter();
        EvenNumberPrinter evenNumberPrinter = new EvenNumberPrinter();
        oddNumberPrinter.start();
        evenNumberPrinter.start();
    }
}

class Printer {
    public static int count = 1;
    public static boolean isOddTurn = true;
    public static final Object lock = new Object();
}

class OddNumberPrinter extends Thread {
    @Override
    public void run() {
        while (true) {
            synchronized (Printer.lock) {
                while (!Printer.isOddTurn) {
                    try {
                        Printer.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Printer.count++);
                Printer.isOddTurn = false;
                Printer.lock.notify();
            }
            if (Printer.count > 10)
                break;
        }
    }
}

class EvenNumberPrinter extends Thread {
    @Override
    public void run() {
        while (true) {
            synchronized (Printer.lock) {
                while (Printer.isOddTurn) {
                    try {
                        Printer.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Printer.count++);
                Printer.isOddTurn = true;
                Printer.lock.notify();
            }
            if (Printer.count > 10)
                break;
        }
    }
}
