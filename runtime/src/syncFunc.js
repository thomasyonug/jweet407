/* Generated from Java with JSweet 4.0.0-SNAPSHOT - http://www.jsweet.org */
class __mData {
}
__mData.m = 1;
var mData = buildProxy(__mData);
mData["__class"] = "mData";
class syncFunc extends WebWorker {
    constructor() {
        super(...arguments);
        this.__captured_cvs = { 'mData.m': mData.m, };
        this.source = function () {
            class __mData {
                constructor(__parent) {
                    this.__parent = __parent;
                }
            }
            __mData.m = 1;
            var mData = buildProxy(__mData);
            mData["__class"] = "mData";
            class syncFunc {
                /**
                 *
                 */
                run() {
                    this.addnum();
                }
                addnum() {
                    Comm.sync(this);
                    let i = 0;
                    console.info("begin");
                    while ((i !== 1000)) {
                        {
                            mData.m = mData.m + 1;
                            i = i + 1;
                        }
                    }
                    ;
                    console.info(mData.m);
                    console.info("end");
                    Comm.unsync(this);
                }
                static main(args) {
                    const Add = new syncFunc();
                    const t1 = new java.lang.Thread(Add);
                    const t2 = new java.lang.Thread(Add);
                    t1.start();
                    t2.start();
                }
                constructor() {
                    this.__captured_cvs = { 'mData.m': mData.m, };
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
        const Add = new syncFunc();
        const t1 = new java.lang.Thread(Add);
        const t2 = new java.lang.Thread(Add);
        t1.start();
        t2.start();
    }
}
syncFunc.class = { __key: "syncFunc" };
syncFunc.main(null);
