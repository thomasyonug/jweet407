class Logger {
    static debug(message) {
        console.debug(`[DEBUG] ${new Date().toISOString()}: ${message}`);
    }
    static info(message) {
        console.log(`[INFO] ${new Date().toISOString()}: ${message}`);
    }
    static warn(message) {
        console.warn(`[WARN] ${new Date().toISOString()}: ${message}`);
    }
    static error(message) {
        console.error(`[ERROR] ${new Date().toISOString()}: ${message}`);
    }
}

/**
 * 主要用于构建worker之间的隧道
 */
class ChannelRegistry {
    // worker 对应的 cvs
    static workerToCvs = new Map();
    // save worker and it's worker id
    static workerIds = new Map();
    // 每个worker仅注册一次
    static register(worker, workerId, cvs) {
        this.workerIds.set(worker, workerId);
        for (let [w, cvsOfw] of this.workerToCvs) {
            let sameCvs = this.GetSameCvs(cvs, cvsOfw);
            if (sameCvs.size > 0) {
                let channel = new MessageChannel();
                w.postMessage({ 'command': 'connect', 'cvs': sameCvs }, [channel.port2]);
                worker.postMessage({ 'command': 'connect', 'cvs': sameCvs }, [channel.port1]);
                Logger.info(`build a channel between worker:${this.workerIds.get(worker)} and worker:${this.workerIds.get(w)} for cvs:${[...sameCvs].join(',')}`);
            }
        }
        this.workerToCvs.set(worker, cvs);
    }
    /**
     * 从两个cvs中获得相同的cv
     */
    static GetSameCvs(cvs1, cvs2) {
        let result = new Set();
        for (let c of cvs1) {
            if (cvs2.has(c)) {
                result.add(c);
            }
        }
        return result;
    }
}

class WebWorker {
    worker = null;
    cvsSet = new Set();
    workerId = null;
    static workerCounter = 0;
    static nextId() {
        WebWorker.workerCounter += 1;
        return WebWorker.workerCounter;
    }
    init() {
        if (this.worker)
            return;
        // initialize worker
        this.worker = new Worker('./initWorker.js', { type: 'module' });
        // assign the id to every worker
        this.workerId = WebWorker.nextId();
        this.worker.postMessage({ 'command': 'init', 'id': `${this.workerId}` });
        // register the channel of variable in capturedCVS
        for (let key in this.__captured_cvs) {
            this.cvsSet.add(key);
        }
        ChannelRegistry.register(this.worker, this.workerId, this.cvsSet);
    }
    // 注册完成后，启动worker
    start() {
        this.worker.postMessage({ 'command': 'start', 'source': this.source });
    }
}

class Cook extends WebWorker {
    __captured_cvs = { 'Desk.food_flag': 'Desk.food_flag', 'Desk.count': 'Desk.count' };
    // Class definition
    source = `
// Worker thread JavaScript code
class __Desk {
    static food_flag = 0;
    static count = 10;
    static className = 'Desk';
}
var Desk = new Proxy(__Desk, proxyHandler);
console.log('Cooker start!');
Desk.food_flag = 999;
`;
}
class Customer extends WebWorker {
    __captured_cvs = { 'Desk.food_flag': 'Desk.food_flag', 'Desk.count': 'Desk.count' };
    // Class definition
    source = `
// Worker thread JavaScript code
class __Desk {
    static food_flag = 0;
    static count = 10;
    static className = 'Desk';
}
// self.__Desk = __Desk;
var Desk = new Proxy(__Desk, proxyHandler);
console.log('Customer start!');
setTimeout(() => {
    console.log('Desk.food_flag:', Desk.food_flag);
}, 1000)
`;
}
const cooker = new Cook(); // Assuming Cook is a subclass of WebWorker
const customer = new Customer(); // Assuming Cook is a subclass of WebWorker
cooker.init();
customer.init();
cooker.start();
customer.start();

export { Cook, Customer };
