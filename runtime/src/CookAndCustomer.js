/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class CookAndCustomer {
    static main(args) {
        const cook = (() => { let __o = new Cook(); __o.__delegate = new Cook(); return __o; })();
        const customer = (() => { let __o = new Customer(); __o.__delegate = new Customer(); return __o; })();
        cook.start();
        customer.start();
    }
}
CookAndCustomer["__class"] = "CookAndCustomer";
class __Desk {
    static lock_$LI$() { if (Desk.lock == null) {
        Desk.lock = new Object();
    } return Desk.lock; }
}
__Desk.food_flag = 0;
__Desk.count = 10;
var Desk = buildProxy(__Desk);
Desk["__class"] = "Desk";
class Cook extends WebWorker {
    constructor() {
        super(...arguments);
        this.__captured_cvs = { 'Desk.count': Desk.count, 'Desk.lock': Desk.lock, 'Desk.food_flag': Desk.food_flag, };
        this.source = function () {
            class __Desk {
                static lock_$LI$() { if (Desk.lock == null) {
                    Desk.lock = new Object();
                } return Desk.lock; }
                constructor(__parent) {
                    this.__parent = __parent;
                }
            }
            __Desk.food_flag = 0;
            __Desk.count = 10;
            var Desk = buildProxy(__Desk);
            Desk["__class"] = "Desk";
            class Cook extends java.lang.Thread {
                constructor() {
                    super(...arguments);
                    this.__captured_cvs = { 'Desk.count': Desk.count, 'Desk.lock': Desk.lock, 'Desk.food_flag': Desk.food_flag, }; /**
                     *
                     */
                }
                run() {
                    while ((true)) {
                        {
                            Comm.sync((Desk.lock_$LI$()));
                            {
                                if (Desk.count === 0) {
                                    Comm.unsync((Desk.lock_$LI$()));
                                    break;
                                }
                                else {
                                    if (Desk.food_flag === 1) {
                                        try {
                                            Desk.lock_$LI$().wait();
                                        }
                                        catch (e) {
                                            throw Object.defineProperty(new Error(e.message), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                        }
                                    }
                                    else {
                                        console.info("\u53a8\u5e08\u505a\u996d");
                                        Desk.food_flag = 1;
                                    }
                                    Desk.lock_$LI$().notifyAll();
                                }
                            }
                            Comm.unsync((Desk.lock_$LI$()));
                            ;
                        }
                    }
                    ;
                }
            }
            Cook["__class"] = "Cook";
            var __entry = new Cook();
            __entry.start();
        };
    }
}
class Customer extends WebWorker {
    constructor() {
        super(...arguments);
        this.__captured_cvs = { 'Desk.count': Desk.count, 'Desk.lock': Desk.lock, 'Desk.food_flag': Desk.food_flag, };
        this.source = function () {
            class __Desk {
                static lock_$LI$() { if (Desk.lock == null) {
                    Desk.lock = new Object();
                } return Desk.lock; }
                constructor(__parent) {
                    this.__parent = __parent;
                }
            }
            __Desk.food_flag = 0;
            __Desk.count = 10;
            var Desk = buildProxy(__Desk);
            Desk["__class"] = "Desk";
            class Customer extends java.lang.Thread {
                constructor() {
                    super(...arguments);
                    this.__captured_cvs = { 'Desk.count': Desk.count, 'Desk.lock': Desk.lock, 'Desk.food_flag': Desk.food_flag, }; /**
                     *
                     */
                }
                run() {
                    while ((true)) {
                        {
                            Comm.sync((Desk.lock_$LI$()));
                            {
                                if (Desk.count === 0) {
                                    Comm.unsync((Desk.lock_$LI$()));
                                    break;
                                }
                                else {
                                    if (Desk.food_flag === 0) {
                                        try {
                                            Desk.lock_$LI$().wait();
                                        }
                                        catch (e) {
                                            throw Object.defineProperty(new Error(e.message), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                        }
                                    }
                                    else {
                                        console.info("\u5ba2\u4eba\u5403\u996d");
                                        Desk.count--;
                                        console.info("\u8fd8\u8981\u5403" + Desk.count + "\u7897");
                                        Desk.food_flag = 0;
                                    }
                                    Desk.lock_$LI$().notifyAll();
                                }
                            }
                            Comm.unsync((Desk.lock_$LI$()));
                            ;
                        }
                    }
                    ;
                }
            }
            Customer["__class"] = "Customer";
            var __entry = new Customer();
            __entry.start();
        };
    }
}
CookAndCustomer.main(null);
