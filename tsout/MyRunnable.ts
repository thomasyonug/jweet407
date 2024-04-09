/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class MyRunnable extends WebWorker{
public __captured_cvs : any = {};

static class= {__key : "MyRunnable"};

public static main(args: string[]) {
    const myRunnable: java.lang.Thread = new MyRunnable();
    const thread: java.lang.Thread = new java.lang.Thread(myRunnable);
    thread.start();
}
public source = function () {
/**
 * 多线程创建：实现Runnable接口
 * 
 * @author mikechen
 * @class
 */
class MyRunnable implements java.lang.Thread {
    public __captured_cvs : any = {};
    static class= {__key : "MyRunnable"};
    /*private*/ i: number;

    /**
     * 
     */
    public run() {
        for(this.i = 0; this.i < 10; this.i++) {{
            console.info("Runnable " + this.i);
        };}
    }

    public static main(args: string[]) {
        const myRunnable: java.lang.Thread = new MyRunnable();
        const thread: java.lang.Thread = new java.lang.Thread(myRunnable);
        thread.start();
    }

    constructor() {
        this.i = 0;
    }
}
MyRunnable["__class"] = "MyRunnable";
MyRunnable["__interfaces"] = ["java.lang.Runnable"];

var __entry = new MyRunnable(); __entry.run();
}}



MyRunnable.main(null);
