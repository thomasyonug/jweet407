
import sun.security.krb5.internal.crypto.Des;

import java.util.concurrent.locks.StampedLock;

public class StampedLockTest {
    public static void main(String[] args) {
        Writer writer = new Writer();
        Reader reader = new Reader();
        writer.start();
        reader.start();
    }
}

class Desk {
    static StampedLock stampedLock = new StampedLock();
    static int count;

}

class Writer extends Thread {
    @Override
    public void run() {
        while(true){
            if(Desk.count<10){
                long l1 = Desk.stampedLock.writeLock();
                try{
                    Desk.count++;
                    System.out.println("Writer writes: Count= " + Desk.count);
                }finally {
                    Desk.stampedLock.unlockWrite(l1);
                }
            }else{
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

class Reader extends Thread {
    @Override
    public void run() {
        while(true){
            if(Desk.count<10) {
                long l1 = Desk.stampedLock.tryOptimisticRead();
                int tmp1 = Desk.count;
                if (Desk.stampedLock.validate(l1)) {
                    System.out.println("Reader reads: Count= " + tmp1);
                } else {
                    long l2 = Desk.stampedLock.readLock();
                    try {
                        int tmp2 = Desk.count;
                        System.out.println("OptimisticRead failed Count = " + tmp1 + " Read Count" + tmp2);
                    } finally {
                        Desk.stampedLock.unlockRead(l2);
                    }
                }
            }else{
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}