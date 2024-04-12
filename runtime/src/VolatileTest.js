/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class __Data {
}
__Data.__captured_volatile_cvs = { 'stop': 'stop' };
__Data.class = { __key: "Data" };
__Data.stop = false;
__Data.cntThread = null;
var Data = buildProxy(__Data);
Data["__class"] = "Data";
class VolatileTest {
    static main(args) {
        const testThread = (() => { let __o = new TestThread(); __o.__delegate = new TestThread(); return __o; })();
        testThread.start();
        const testThread2 = (() => { let __o = new TestThread2(); __o.__delegate = new TestThread2(); return __o; })();
        testThread2.start();
        console.info("now, in main thread stop is: " + Data.stop);
    }
}
VolatileTest["__class"] = "VolatileTest";
class TestThread extends WebWorker {
    constructor() {
        super(...arguments);
        this.__captured_cvs = { 'Data.stop': Data.stop, 'Data.cntThread': Data.cntThread, };
        this.source = function () {
            class __Data {
                constructor(__parent) {
                    this.__parent = __parent;
                }
            }
            __Data.__captured_volatile_cvs = { 'stop': 'stop' };
            __Data.class = { __key: "Data" };
            __Data.stop = false;
            __Data.cntThread = null;
            var Data = buildProxy(__Data);
            Data["__class"] = "Data";
            class TestThread extends java.lang.Thread {
                constructor() {
                    super(...arguments);
                    this.__captured_cvs = { 'Data.stop': Data.stop, 'Data.cntThread': Data.cntThread, };
                }
                /**
                 *
                 */
                run() {
                    Data.cntThread = this;
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
            TestThread.class = { __key: "TestThread" };
            TestThread["__class"] = "TestThread";
            var __entry = new TestThread();
            __entry.run();
        };
    }
}
TestThread.class = { __key: "TestThread" };
class TestThread2 extends WebWorker {
    constructor() {
        super(...arguments);
        this.__captured_cvs = { 'Data.stop': Data.stop, 'Data.cntThread': Data.cntThread, };
        this.source = function () {
            class __Data {
                constructor(__parent) {
                    this.__parent = __parent;
                }
            }
            __Data.__captured_volatile_cvs = { 'stop': 'stop' };
            __Data.class = { __key: "Data" };
            __Data.stop = false;
            __Data.cntThread = null;
            var Data = buildProxy(__Data);
            Data["__class"] = "Data";
            class TestThread2 extends java.lang.Thread {
                constructor() {
                    super(...arguments);
                    this.__captured_cvs = { 'Data.stop': Data.stop, 'Data.cntThread': Data.cntThread, };
                }
                /**
                 *
                 */
                run() {
                    try {
                        java.lang.Thread.sleep(100);
                    }
                    catch (e) {
                        throw Object.defineProperty(new Error(e.message), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    Data.stop = true;
                    console.info("Thread2 update stop to: " + Data.stop);
                    if (Data.cntThread != null) {
                        console.info("Thread2 waiting for Thread1 to stop");
                        try {
                            Data.cntThread.join();
                        }
                        catch (e) {
                            throw Object.defineProperty(new Error(e.message), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                        }
                    }
                }
            }
            TestThread2.class = { __key: "TestThread2" };
            TestThread2["__class"] = "TestThread2";
            var __entry = new TestThread2();
            __entry.run();
        };
    }
}
TestThread2.class = { __key: "TestThread2" };
VolatileTest.main(null);
