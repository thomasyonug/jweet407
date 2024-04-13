/*
 *自行车组装厂
 * 一个生产者生产轮子，一个生产者生产车架
 * 组装者使用两个轮子和一个车架进行组装
 */
public class BicycleProduction {
    public static void main(String[] args) {
        FrameProducer frameProducer = new FrameProducer();
        WheelProducer wheelProducer = new WheelProducer();
        BicycleAssembly bicycleAssembly = new BicycleAssembly();
        frameProducer.start();
        wheelProducer.start();
        bicycleAssembly.start();
    }
}
class Warehouse{
    public static int assumblyNum = 5;
    public static int frameFlag = 0;
    public static int wheelFlag = 0;
    public static Object lock = new Object();
}
class FrameProducer extends Thread{
    @Override
    public void run() {
        while (true){
            synchronized (Warehouse.lock){
                if(Warehouse.assumblyNum == 0){
                    break;
                }
                else {
                    if(Warehouse.frameFlag ==1){
                        try {
                            Warehouse.lock.wait(); //仓库已有车架
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        System.out.println("生产车架");
                        Warehouse.frameFlag = 1;
                    }
                    Warehouse.lock.notifyAll();
                }
            }
        }
    }
}
class WheelProducer extends Thread{
    @Override
    public void run() {
        while (true){
            synchronized (Warehouse.lock){
                if(Warehouse.assumblyNum == 0){
                    break;
                }
                else {
                    if(Warehouse.wheelFlag ==2){
                        try {
                            Warehouse.lock.wait(); //仓库已有车轮
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else{
                        System.out.println("生产车轮");
                        Warehouse.wheelFlag +=1;
                    }
                    Warehouse.lock.notifyAll();
                }
            }
        }
    }
}
class BicycleAssembly extends Thread{
    @Override
    public void run() {
        while (true){
            synchronized (Warehouse.lock){
                if(Warehouse.assumblyNum ==0){
                    break;
                }
                else{
                    while(Warehouse.frameFlag!=1 || Warehouse.wheelFlag!=2){
                        try {
                            Warehouse.lock.wait(); //车轮或车架数不够
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }


                    Warehouse.frameFlag = 0;
                    Warehouse.wheelFlag = 0;
                    Warehouse.assumblyNum--;
                    System.out.println("使用一个车架两个轮子组装自行车,还需组装"+Warehouse.assumblyNum+"辆");
                    Warehouse.lock.notifyAll();
                }
            }
        }
    }
}