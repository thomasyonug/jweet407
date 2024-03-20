const Logger = require('./Logger.js');

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

module.exports = ChannelRegistry