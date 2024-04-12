class Printer {
    private final Object lock = new Object();

    public void printDocument(String documentName, int duration) {
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() + " started printing: " + documentName);
            try {
                Thread.sleep(duration); // Simulate the time taken to print
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished printing: " + documentName);
        }
    }
}

class UserThread extends Thread {
    private Printer printer;
    private String documentName;
    private int duration;

    public UserThread(Printer printer, String documentName, int duration, String name) {
        super(name); // Set the thread (user) name
        this.printer = printer;
        this.documentName = documentName;
        this.duration = duration;
    }

    @Override
    public void run() {
        printer.printDocument(documentName, duration); // Try to print
    }
}

public class SharedPrinterExample {
    public static void main(String[] args) {
        Printer sharedPrinter = new Printer();

        UserThread user1 = new UserThread(sharedPrinter, "Document1.pdf", 1000, "User1");
        UserThread user2 = new UserThread(sharedPrinter, "Document2.pdf", 800, "User2");
        UserThread user3 = new UserThread(sharedPrinter, "Document3.pdf", 1200, "User3");

        user1.start();
        user2.start();
        user3.start();

        try {
            user1.join();
            user2.join();
            user3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All documents have been printed.");
    }
}

class A {
	public Thread t1
	public Thread t2 
}

run() {
	A.t1 = new Thread();
	A.t2 = new Thread();
}

Thread {
	Comm.join(workerId);
	A.t2.join();
}