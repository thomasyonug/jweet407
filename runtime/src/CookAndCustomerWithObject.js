/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class CookAndCustomerWithObject {
    static main(args) {
        const cook = (() => { let __o = new Cook(); __o.__delegate = new Cook(); return __o; })();
        const customer = (() => { let __o = new Customer(); __o.__delegate = new Customer(); return __o; })();
        cook.start();
        customer.start();
    }
}
CookAndCustomerWithObject["__class"] = "CookAndCustomerWithObject";
class __Food {
    constructor() {
        this.food_flag = 0;
        this.count = 10;
    }
}
var Food = buildProxy(__Food);
Food["__class"] = "Food";
class __Desk {
    static food_$LI$() { if (Desk.food == null) {
        Desk.food = new Food();
    } return Desk.food; }
    static lock_$LI$() { if (Desk.lock == null) {
        Desk.lock = new Object();
    } return Desk.lock; }
}
var Desk = buildProxy(__Desk);
Desk["__class"] = "Desk";
class Cook extends WebWorker {
    constructor() {
        super(...arguments);
        this.__captured_cvs = { 'Desk.lock': Desk.lock, 'java.lang.Object': java.lang.Object, 'Desk.food': Desk.food, 'Food': Food, };
        this.source = function () {
            class __Food {
                constructor(__parent) {
                    this.__parent = __parent;
                    this.food_flag = 0;
                    this.count = 10;
                }
            }
            var Food = buildProxy(__Food);
            Food["__class"] = "Food";
            class __Desk {
                static food_$LI$() { if (Desk.food == null) {
                    Desk.food = new Food();
                } return Desk.food; }
                static lock_$LI$() { if (Desk.lock == null) {
                    Desk.lock = new Object();
                } return Desk.lock; }
                constructor(__parent) {
                    this.__parent = __parent;
                }
            }
            var Desk = buildProxy(__Desk);
            Desk["__class"] = "Desk";
            class Cook extends java.lang.Thread {
                constructor() {
                    super(...arguments);
                    this.__captured_cvs = { 'Desk.lock': Desk.lock, 'java.lang.Object': java.lang.Object, 'Desk.food': Desk.food, 'Food': Food, };
                }
                /**
                 *
                 */
                run() {
                    while ((true)) {
                        {
                            Comm.sync((Desk.lock_$LI$()));
                            {
                                if (Desk.food_$LI$().count === 0) {
                                    Comm.unsync((Desk.lock_$LI$()));
                                    break;
                                }
                                else {
                                    if (Desk.food_$LI$().food_flag === 1) {
                                        try {
                                            Desk.lock_$LI$().wait();
                                        }
                                        catch (e) {
                                            throw Object.defineProperty(new Error(e.message), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                        }
                                    }
                                    else {
                                        console.info("\u9358\u3125\u7b00\u934b\u6c36\u30ad");
                                        Desk.food_$LI$().food_flag = 1;
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
            Cook.class = { __key: "Cook" };
            Cook["__class"] = "Cook";
            var __entry = new Cook();
            __entry.run();
        };
    }
}
Cook.class = { __key: "Cook" };
class Customer extends WebWorker {
    constructor() {
        super(...arguments);
        this.__captured_cvs = { 'Desk.lock': Desk.lock, 'java.lang.Object': java.lang.Object, 'Desk.food': Desk.food, 'Food': Food, };
        this.source = function () {
            class __Food {
                constructor(__parent) {
                    this.__parent = __parent;
                    this.food_flag = 0;
                    this.count = 10;
                }
            }
            var Food = buildProxy(__Food);
            Food["__class"] = "Food";
            class __Desk {
                static food_$LI$() { if (Desk.food == null) {
                    Desk.food = new Food();
                } return Desk.food; }
                static lock_$LI$() { if (Desk.lock == null) {
                    Desk.lock = new Object();
                } return Desk.lock; }
                constructor(__parent) {
                    this.__parent = __parent;
                }
            }
            var Desk = buildProxy(__Desk);
            Desk["__class"] = "Desk";
            class Customer extends java.lang.Thread {
                constructor() {
                    super(...arguments);
                    this.__captured_cvs = { 'Desk.lock': Desk.lock, 'java.lang.Object': java.lang.Object, 'Desk.food': Desk.food, 'Food': Food, };
                }
                /**
                 *
                 */
                run() {
                    while ((true)) {
                        {
                            Comm.sync((Desk.lock_$LI$()));
                            {
                                if (Desk.food_$LI$().count === 0) {
                                    Comm.unsync((Desk.lock_$LI$()));
                                    break;
                                }
                                else {
                                    if (Desk.food_$LI$().food_flag === 0) {
                                        try {
                                            Desk.lock_$LI$().wait();
                                        }
                                        catch (e) {
                                            throw Object.defineProperty(new Error(e.message), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                                        }
                                    }
                                    else {
                                        console.info("\u7039\ue76d\u6c49\u935a\u51ae\u30ad");
                                        Desk.food_$LI$().count--;
                                        console.info("\u6769\u6a3f\ue6e6\u935a\ufffd" + Desk.food_$LI$().count + "\u7eb0\ufffd");
                                        Desk.food_$LI$().food_flag = 0;
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
            Customer.class = { __key: "Customer" };
            Customer["__class"] = "Customer";
            var __entry = new Customer();
            __entry.run();
        };
    }
}
Customer.class = { __key: "Customer" };
CookAndCustomerWithObject.main(null);
