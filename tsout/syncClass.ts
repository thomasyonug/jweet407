/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class syncClass extends WebWorker{
public __captured_cvs : any = {};

static class= {__key : "syncClass"};

public static compare(a: number, b: number): number {
    Comm.sync(syncClass.class);
    if (a > b){
        Comm.unsync(syncClass.class);
return 0;
    } else if (a === b){
        Comm.unsync(syncClass.class);
return 1;
    } else {
        Comm.unsync(syncClass.class);
return 2;
    }
    Comm.unsync(syncClass.class);
}public static main(args: string[]) {
    const Com: java.lang.Thread = new syncClass();
    const t1: java.lang.Thread = new java.lang.Thread(Com);
    const t2: java.lang.Thread = new java.lang.Thread(Com);
    t1.start();
    t2.start();
}
public source = function () {
class syncClass implements java.lang.Thread {
    public __captured_cvs : any = {};
    static class= {__key : "syncClass"};
    /**
     * 
     */
    public run() {
        syncClass.compare(5, 6);
    }

    public static compare(a: number, b: number): number {
        Comm.sync(syncClass.class);
        if (a > b){
            Comm.unsync(syncClass.class);
return 0;
        } else if (a === b){
            Comm.unsync(syncClass.class);
return 1;
        } else {
            Comm.unsync(syncClass.class);
return 2;
        }
        Comm.unsync(syncClass.class);
    }

    public static main(args: string[]) {
        const Com: java.lang.Thread = new syncClass();
        const t1: java.lang.Thread = new java.lang.Thread(Com);
        const t2: java.lang.Thread = new java.lang.Thread(Com);
        t1.start();
        t2.start();
    }

    constructor() {
    }
}
syncClass["__class"] = "syncClass";
syncClass["__interfaces"] = ["java.lang.Runnable"];

var __entry = new syncClass(); __entry.run();
}}



syncClass.main(null);
