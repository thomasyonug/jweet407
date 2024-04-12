/*
*多组演员在剧院演出，观众观看 剧院累计表演5次后结束
* Actor 演员 其他演员表演时被阻塞
* Audience 观众 没有演员表演时被阻塞
* Theater 剧院
 */


public class ActorAndWacher {
    public static void main(String[] args) {
        // 演员和观众的例子
        Theater.performanceFlag = 0;
        Actor actor1 = new Actor();
        Actor actor2 = new Actor();
        Audience audience = new Audience();
        // actor1.setName("演员a");
        // actor2.setName("演员b");
        actor1.start();
        actor2.start();
        audience.start();
    }
}
class Theater {
    public static int performanceFlag; // 表演标志，1为正在演出，0为未演出
    public static int showCount = 5; // 表演次数
    public static Object lock = new Object(); // 锁
}

class Actor extends Thread {
    @Override
    public void run() {
        while (true) {
            synchronized (Theater.lock) {
                if(Theater.showCount == 0){
                    break;
                }
                else {
                    if(Theater.performanceFlag==1){
                        try {
                            Theater.lock.wait(); // 演员正在表演
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        System.out.println("演员演出");
                        Theater.showCount--;
                        Theater.performanceFlag = 1;
                    }
                    Theater.lock.notifyAll(); // 通知观众观看
                }


            }
        }
    }
}

class Audience extends Thread {
    @Override
    public void run() {
        while (true) {
            synchronized (Theater.lock) {
                if(Theater.showCount == 0){
                    break;
                }
                else{
                    if (Theater.performanceFlag == 0) {
                        try {
                            Theater.lock.wait(); // 没有演员演出，观众等待
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }else {
                        System.out.println("观众观看演出");
                        Theater.performanceFlag = 0;
                    }
                    Theater.lock.notifyAll(); // 通知演员演出
                }

            }
        }
    }
}

