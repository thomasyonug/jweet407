class ChannelCenter {
    // every worker holds a channel
    //private static channels: Map<string, Set<MessagePort>> = new Map();
    // 此map仅为防止创建重复channel
    //private static workerToPort: Map<Worker, MessagePort> = new Map();
    //cvs对应的workers
    private static  cvsToWorkers: Map<string, Set<Worker>> = new Map();
    static register(worker: Worker, key: string) {
        // 如果这个cvs对应的worker集合不存在，创建一个
        if (!this.cvsToWorkers.has(key)) {
            this.cvsToWorkers.set(key, new Set());
        }
        // 将worker放入cvsToWorkers[key]
        this.cvsToWorkers.get(key)!.add(worker);
        //worker与Set中所有worker建立channel
        for (let w of this.cvsToWorkers.get(key)!) {
            if (w !== worker) {
                let channel = new MessageChannel();
                w.postMessage({ 'command': 'connect','key': key}, [ channel.port2 ]);
                worker.postMessage({ 'command': 'connect','key':key }, [ channel.port1 ]);
                console.log(`connect ${key} to ${w}`)
            }
        }


        // 每个worker对应的port的一端
        //let port: MessagePort | undefined = this.workerToPort.get(worker);

        // 如果这个worker是第一次建立隧道，需要进行connect操作
        // if (port === undefined) {
        //     let channel = new MessageChannel();
        //     this.workerToPort.set(worker, channel.port1);
        //     // 将另一个port传给worker
        //     worker.postMessage({ 'command': 'connect' }, [ channel.port2 ]);
        //     port = this.workerToPort.get(worker)!;
        //
        //     // 该port需要能接受set指令，放在if里，防止多次定义
        //     port.onmessage = ev => {
        //         switch (ev.data.command) {
        //             case "set":
        //                 this.update(ev.data.key, ev.data.value)
        //         }
        //     }
        // }

        // 更新channels
        // if (!this.channels.has(key)) {
        //     this.channels.set(key, new Set());
        // }
        // this.channels.get(key)!.add(port);
    }

    // 更新key对应的变量，发送变量到各个worker
    // static update(key: string, value: any) {
    //     if (!this.channels.has(key)) {
    //         return
    //     }
    //     // console.log(`update the ${key}`)
    //     // console.log(value)
    //     for (let port of this.channels.get(key)!) {
    //         port.postMessage({ command: 'update', key, value })
    //     }
    // }
}

export default ChannelCenter;