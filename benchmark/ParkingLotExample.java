/*
一个简单的停车场管理系统，有多个车辆（线程）尝试进入停车场，但停车场只能容纳有限数量的车辆
 */
public class ParkingLotExample {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Car car = new Car("Car" + (i + 1));
            car.start();
        }
    }
}

class ParkingLot {
    public static final int CAPACITY = 5;
    public static int parkedCars = 0;
    public static final Object lock = new Object();
    public static boolean parkingAvailable = true;
}

class Car extends Thread {
    public Car(String name) {
        super(name);
    }

    @Override
    public void run() {
        synchronized (ParkingLot.lock) {
            while (!ParkingLot.parkingAvailable || ParkingLot.parkedCars >= ParkingLot.CAPACITY) {
                try {
                    ParkingLot.lock.wait(); // 阻塞直到停车场有空位
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ParkingLot.parkedCars++;
            System.out.println(Thread.currentThread().getName() + " enters the parking lot. Total parked cars: " + ParkingLot.parkedCars);
            if (ParkingLot.parkedCars == ParkingLot.CAPACITY) {
                ParkingLot.parkingAvailable = false; // 停车场已满，设置为不可用
            }
        }

        try {
            Thread.sleep((long) (Math.random() * 5000)); // 模拟车辆在停车场停留的时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (ParkingLot.lock) {
            ParkingLot.parkedCars--;
            System.out.println(Thread.currentThread().getName() + " leaves the parking lot. Total parked cars: " + ParkingLot.parkedCars);
            if (ParkingLot.parkedCars == 0) {
                ParkingLot.parkingAvailable = true; // 停车场为空，设置为可用
                ParkingLot.lock.notifyAll(); // 唤醒其他等待进入停车场的车辆线程
            }
        }
    }
}
