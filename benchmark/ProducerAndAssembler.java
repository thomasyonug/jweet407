/*
*三个生产者生产三种不同产品
* 组装者使用三种产品进行组装
 */

public class ProducerAndAssembler {
    public static void main(String[] args) {
        ProducerA producerA = new ProducerA();
        ProducerB producerB = new ProducerB();
        ProducerC producerC = new ProducerC();
        Assembler assembler = new Assembler();
        assembler.start();
        producerA.start();
        producerB.start();
        producerC.start();

    }
}
class Warehouse{
    public static int AFlag = 0;
    public static int BFlag = 0;
    public static int CFlag = 0;
    public static Object lock = new Object();
    public static int num = 5;
}
class ProducerA extends Thread{

    @Override
    public void run() {
        while(true){
            synchronized (Warehouse.lock){
                if(Warehouse.num ==0){
                    break;
                }
                else{
                    if(Warehouse.AFlag ==1){
                        try {
                            Warehouse.lock.wait(); //仓库已有零件A
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        System.out.println("ProducerA生产A");
                        Warehouse.AFlag = 1;
                    }
                    Warehouse.lock.notifyAll();

                }
            }
        }
    }
}
class ProducerB extends Thread{

    @Override
    public void run() {
        while(true){
            synchronized (Warehouse.lock){
                if(Warehouse.num ==0){
                    break;
                }
                else{
                    if(Warehouse.BFlag ==1){
                        try {
                            Warehouse.lock.wait(); //仓库已有零件B
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        System.out.println("ProducerB生产B");
                        Warehouse.BFlag = 1;
                    }
                    Warehouse.lock.notifyAll();
                }
            }
        }
    }
}
class ProducerC extends Thread{

    @Override
    public void run() {
        while(true){
            synchronized (Warehouse.lock){
                if(Warehouse.num ==0){
                    break;
                }
                else{
                    if(Warehouse.CFlag ==1){
                        try {
                            Warehouse.lock.wait(); //仓库已有零件C
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        System.out.println("ProducerC生产C");
                        Warehouse.CFlag = 1;
                    }
                    Warehouse.lock.notifyAll();
                }
            }
        }
    }
}
class Assembler extends Thread{
    @Override
    public void run() {
        while (true){
            synchronized (Warehouse.lock){
                if(Warehouse.num ==0){
                    break;
                }
                else{
                    while(Warehouse.AFlag ==0||Warehouse.BFlag ==0||Warehouse.CFlag ==0){
                        try {
                            Warehouse.lock.wait(); //零件A，B或C数量不够
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    System.out.println("ProducerD组装ABC");
                    Warehouse.AFlag = 0;
                    Warehouse.BFlag = 0;
                    Warehouse.CFlag = 0;
                    Warehouse.num--;
                    Warehouse.lock.notifyAll();
                }
            }
        }
    }
}