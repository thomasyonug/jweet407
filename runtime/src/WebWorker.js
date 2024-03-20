const ChannelRegistry = require('./ChannelRegistry.js');

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
        this.worker = new Worker('./initWorker.js');
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

module.exports = WebWorker