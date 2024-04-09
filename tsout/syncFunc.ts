/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class __Data {
    public static m: number = 3;

    public static n: number = 2;
}var Data = buildProxy(__Data);

Data["__class"] = "Data";


class syncFunc extends WebWorker{
public __captured_cvs : any = {'Data.n':Data.n,'Data.m':Data.m,};

static class= {__key : "syncFunc"};

public static main(args: string[]) {
    const Com: java.lang.Thread = new syncFunc();
    const t1: java.lang.Thread = new java.lang.Thread(Com);
    const t2: java.lang.Thread = new java.lang.Thread(Com);
    t1.start();
    Data.n = Data.n + 2;
    t2.start();
}
public source = function () {
class __Data {
    public __parent: any;
    public static m: number = 3;

    public static n: number = 2;

    constructor(__parent: any) {
        this.__parent = __parent;
    }
}var Data = buildProxy(__Data);

Data["__class"] = "Data";
class syncFunc implements java.lang.Thread {
    public __captured_cvs : any = {'Data.n':Data.n,'Data.m':Data.m,};
    static class= {__key : "syncFunc"};
    /**
     * 
     */
    public run() {
        this.compare(Data.m, Data.n);
    }

    public compare(a: number, b: number): number {
        Comm.sync(this);
        if (a > b){
            console.info("a>b");
            Comm.unsync(this);
return 0;
        } else if (a === b){
            console.info("a=b");
            Comm.unsync(this);
return 1;
        } else {
            console.info("a<b");
            Comm.unsync(this);
return 2;
        }
        Comm.unsync(this);
    }

    public static main(args: string[]) {
        const Com: java.lang.Thread = new syncFunc();
        const t1: java.lang.Thread = new java.lang.Thread(Com);
        const t2: java.lang.Thread = new java.lang.Thread(Com);
        t1.start();
        Data.n = Data.n + 2;
        t2.start();
    }

    constructor() {
    }
}
syncFunc["__class"] = "syncFunc";
syncFunc["__interfaces"] = ["java.lang.Runnable"];

var __entry = new syncFunc(); __entry.run();
}}



syncFunc.main(null);
