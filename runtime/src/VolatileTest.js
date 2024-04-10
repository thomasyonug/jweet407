/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class __Data {
}
__Data.__captured_volatile_cvs = { 'stop': 'stop' };
__Data.stop = false;
var Data = buildProxy(__Data);
Data["__class"] = "Data";
class VolatileTest {
    static main(args) {
        const testThread = (() => { let __o = new TestThread(); __o.__delegate = new TestThread(); return __o; })();
        testThread.start();
        //java.lang.Thread.sleep(100);
        const testThread2 = (() => { let __o = new TestThread2(); __o.__delegate = new TestThread2(); return __o; })();
        testThread2.start();
        console.info("now, in main thread stop is: " + Data.stop);
        //testThread.join();
        //testThread2.join();
    }
}
VolatileTest["__class"] = "VolatileTest";
class TestThread extends WebWorker {
    constructor() {
        super(...arguments);
        this.__captured_cvs = { 'Data.stop': Data.stop, };
        this.source = function () {
            class __Data {
                constructor(__parent) {
                    this.__parent = __parent;
                }
            }
            __Data.__captured_volatile_cvs = { 'stop': 'stop' };
            __Data.stop = false;
            var Data = buildProxy(__Data);
            Data["__class"] = "Data";
            class TestThread extends java.lang.Thread {
                constructor() {
                    super(...arguments);
                    this.__captured_cvs = { 'Data.stop': Data.stop, };
                }
                /**
                 *
                 */
                run() {
                    let i = 1;
                    while ((!Data.stop)) {
                        {
                            i++;
                        }
                    }
                    ;
                    console.info("Thread1 stop: i=" + i);
                }
            }
            TestThread["__class"] = "TestThread";
            var __entry = new TestThread();
            __entry.run();
        };
    }
}
class TestThread2 extends WebWorker {
    constructor() {
        super(...arguments);
        this.__captured_cvs = { 'Data.stop': Data.stop, };
        this.source = function () {
            class __Data {
                constructor(__parent) {
                    this.__parent = __parent;
                }
            }
            __Data.__captured_volatile_cvs = { 'stop': 'stop' };
            __Data.stop = false;
            var Data = buildProxy(__Data);
            Data["__class"] = "Data";
            class TestThread2 extends java.lang.Thread {
                constructor() {
                    super(...arguments);
                    this.__captured_cvs = { 'Data.stop': Data.stop, };
                }
                /**
                 *
                 */
                run() {
                    Data.stop = true;
                    console.info("Thread2 update stop to: " + Data.stop);
                }
            }
            TestThread2["__class"] = "TestThread2";
            var __entry = new TestThread2();
            __entry.run();
        };
    }
}
VolatileTest.main(null);
