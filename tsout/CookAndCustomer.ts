/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class CookAndCustomer {
    public static main(args: string[]) {
        const cook: Cook = (() => { let __o : any = new Cook(); __o.__delegate = new Cook(); return __o; })();
        const customer: Customer = (() => { let __o : any = new Customer(); __o.__delegate = new Customer(); return __o; })();
        cook.start();
        customer.start();
    }
}
CookAndCustomer["__class"] = "CookAndCustomer";


class __Desk {
    public static food_flag: number = 0;

    public static count: number = 10;

    public static lock: any; public static lock_$LI$(): any { if (Desk.lock == null) { Desk.lock = new Object(); }  return Desk.lock; }
}var Desk = buildProxy(__Desk);

Desk["__class"] = "Desk";


class Cook extends WebWorker{
public __captured_cvs : any = {'Desk.count':Desk.count,'Desk.lock':Desk.lock,'Desk.food_flag':Desk.food_flag,};

static class= {__key : "Cook"};


public source = function () {
class __Desk {
    public __parent: any;
    public static food_flag: number = 0;

    public static count: number = 10;

    public static lock: any; public static lock_$LI$(): any { if (Desk.lock == null) { Desk.lock = new Object(); }  return Desk.lock; }

    constructor(__parent: any) {
        this.__parent = __parent;
    }
}var Desk = buildProxy(__Desk);

Desk["__class"] = "Desk";
class Cook extends java.lang.Thread {
    public __captured_cvs : any = {'Desk.count':Desk.count,'Desk.lock':Desk.lock,'Desk.food_flag':Desk.food_flag,};
    static class= {__key : "Cook"};
    /**
     * 
     */
    public run() {
        while((true)) {{
            Comm.sync((Desk.lock_$LI$()));{
                if (Desk.count === 0){
                    Comm.unsync((Desk.lock_$LI$()));break;
                } else {
                    if (Desk.food_flag === 1){
                        try {
                            Desk.lock_$LI$().wait();
                        } catch(e) {
                            throw Object.defineProperty(new Error(e.message), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
                        }
                    } else {
                        console.info("\u53a8\u5e08\u505a\u996d");
                        Desk.food_flag = 1;
                    }
                    Desk.lock_$LI$().notifyAll();
                }
            }Comm.unsync((Desk.lock_$LI$()));;
        }};
    }
}
Cook["__class"] = "Cook";
var __entry = new Cook(); __entry.run();
}}

class Customer extends WebWorker{
public __captured_cvs : any = {'Desk.count':Desk.count,'Desk.lock':Desk.lock,'Desk.food_flag':Desk.food_flag,};

static class= {__key : "Customer"};


public source = function () {
class __Desk {
    public __parent: any;
    public static food_flag: number = 0;

    public static count: number = 10;

    public static lock: any; public static lock_$LI$(): any { if (Desk.lock == null) { Desk.lock = new Object(); }  return Desk.lock; }

    constructor(__parent: any) {
        this.__parent = __parent;
    }
}var Desk = buildProxy(__Desk);

Desk["__class"] = "Desk";
class Customer extends java.lang.Thread {
    public __captured_cvs : any = {'Desk.count':Desk.count,'Desk.lock':Desk.lock,'Desk.food_flag':Desk.food_flag,};
    static class= {__key : "Customer"};
    /**
     * 
     */
    public run() {
        while((true)) {{
            Comm.sync((Desk.lock_$LI$()));{
                if (Desk.count === 0){
                    Comm.unsync((Desk.lock_$LI$()));break;
                } else {
                    if (Desk.food_flag === 0){
                        try {
                            Desk.lock_$LI$().wait();
                        } catch(e) {
                            throw Object.defineProperty(new Error(e.message), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
                        }
                    } else {
                        console.info("\u5ba2\u4eba\u5403\u996d");
                        Desk.count--;
                        console.info("\u8fd8\u8981\u5403" + Desk.count + "\u7897");
                        Desk.food_flag = 0;
                    }
                    Desk.lock_$LI$().notifyAll();
                }
            }Comm.unsync((Desk.lock_$LI$()));;
        }};
    }
}
Customer["__class"] = "Customer";
var __entry = new Customer(); __entry.run();
}}



CookAndCustomer.main(null);
