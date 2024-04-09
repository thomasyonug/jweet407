/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class F {
    public i: number;

    constructor() {
        if (this.i === undefined) { this.i = 0; }
    }
}
F["__class"] = "F";


class A {
    public static main(args: string[]) {
        const t1: T = (() => { let __o : any = new T(); __o.__delegate = new T(); return __o; })();
        const t2: T = (() => { let __o : any = new T(); __o.__delegate = new T(); return __o; })();
        const f: F = new F();
        t1.f = f;
        t2.f = f;
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        console.info(f.i);
    }
}
A["__class"] = "A";


class T extends WebWorker{
public __captured_cvs : any = {};

static class= {__key : "T"};


public source = function () {
class T extends java.lang.Thread {
    public __captured_cvs : any = {};
    static class= {__key : "T"};
    public f: F;

    public j: number;

    public run() {
        while((this.j < 5)) {{
            Comm.sync((this.f));{
                this.f.i++;
                console.info(this.f.i);
            }Comm.unsync((this.f));;
            this.j++;
        }};
    }

    constructor() {
        super();
        if (this.f === undefined) { this.f = null; }
        if (this.j === undefined) { this.j = 0; }
    }
}
T["__class"] = "T";
var __entry = new T(); __entry.run();
}}



A.main(null);
