import ChannelRegistry from "./ChannelRegistry";

abstract class WebWorker {
    protected abstract source: string;
    protected abstract __captured_cvs: any;
    protected worker: Worker | null = null;
    private cvsSet: Set<string> = new Set();
    private workerId: number | null = null;
    
    private static workerCounter: number = 0;
    private static nextId(): number {
        WebWorker.workerCounter += 1;
        return WebWorker.workerCounter;
    }

    init() {
        if (this.worker)
            return

        // initialize worker
        this.worker = new Worker('./initWorker.js', {type: 'module'});
        
        // assign the id to every worker
        this.workerId = WebWorker.nextId();
        this.worker.postMessage({'command': 'init', 'id': `${this.workerId}`});

        // register the channel of variable in capturedCVS
        for (let key in this.__captured_cvs) {
            this.cvsSet.add(key);
        }
        ChannelRegistry.register(this.worker, this.workerId, this.cvsSet);
    }
    
    // 注册完成后，启动worker
    start() {
        this.worker!.postMessage({ 'command': 'start', 'source': this.source });
    }
}

export default WebWorker;