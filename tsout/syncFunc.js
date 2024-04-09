/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class __Data {
}
__Data.m = 3;
__Data.n = 2;
var Data = buildProxy(__Data);
Data["__class"] = "Data";
class syncFunc extends WebWorker {
    constructor() {
        super(...arguments);
        this.__captured_cvs = { 'Data.n': Data.n, 'Data.m': Data.m, };
        this.source = function () {
            class __Data {
                constructor(__parent) {
                    this.__parent = __parent;
                }
            }
            __Data.m = 3;
            __Data.n = 2;
            var Data = buildProxy(__Data);
            Data["__class"] = "Data";
            class syncFunc {
                /**
                 *
                 */
                run() {
                    this.compare(Data.m, Data.n);
                }
                compare(a, b) {
                    Comm.sync(this);
                    if (a > b) {
                        console.info("a>b");
                        Comm.unsync(this);
                        return 0;
                    }
                    else if (a === b) {
                        console.info("a=b");
                        Comm.unsync(this);
                        return 1;
                    }
                    else {
                        console.info("a<b");
                        Comm.unsync(this);
                        return 2;
                    }
                    Comm.unsync(this);
                }
                static main(args) {
                    const Com = new syncFunc();
                    const t1 = new java.lang.Thread(Com);
                    const t2 = new java.lang.Thread(Com);
                    t1.start();
                    Data.n = Data.n + 2;
                    t2.start();
                }
                constructor() {
                    this.__captured_cvs = { 'Data.n': Data.n, 'Data.m': Data.m, };
                }
            }
            syncFunc.class = { __key: "syncFunc" };
            syncFunc["__class"] = "syncFunc";
            syncFunc["__interfaces"] = ["java.lang.Runnable"];
            var __entry = new syncFunc();
            __entry.run();
        };
    }
    static main(args) {
        const Com = new syncFunc();
        const t1 = new java.lang.Thread(Com);
        const t2 = new java.lang.Thread(Com);
        t1.start();
        Data.n = Data.n + 2;
        t2.start();
    }
}
syncFunc.class = { __key: "syncFunc" };
syncFunc.main(null);
