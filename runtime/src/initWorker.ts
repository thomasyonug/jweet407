/**
 * initWorker
 * 一个简单的worker，是每个worker启动的初始状态
 */
import { Logger } from "./Logger";

/**
 * 一系列全局变量，在worker的整个生命周期中都可以被调用
 */
// ============== Start of 全局变量 ==============
// 用来查找key对应的port（也对应一个worker）
let keyToPort: Map<string, Set<MessagePort>> = new Map();
// cvs更新后的值会在这里
let objectMap: Map<string, any> = new Map();
let workerId: number;
let workerName: string;
/**
 * ProxyHandler
 * get：从变量表中查找，如果没有则返回本地值
 * set：从keyToPort中找到对应的port，然后发送给这些port
 */
(self as any).proxyHandler = {
    // Your proxyHandler implementation
    get: function(target: any, propKey: string) {
        //如果在cvs中,并且已经set就返回objectMap的值
        let key= target.className+'.'+propKey;
        if (objectMap.has(key) ){
            console.log('get value:'+key+':'+objectMap.get(key));
            return objectMap.get(key);
        }
        return target[propKey];
    },
    set: function(target: any, propKey: string, newValue: any) {
        target[propKey] = newValue; // may be redundant?
        let key= target.className + '.' + propKey;
        if (!keyToPort.has(key)) { // 如果设置一个新prop，或者根本就没有其他worker共享
            return true; // not implement yet
        }
        for (let port of keyToPort.get(key)!) {
            port.postMessage({'command': 'update', 'key': key, 'value': newValue});
        }
        return true;
    }
};
// ============== End of 全局变量 ================

/**
 * start:   当接受到{ command: 'start', source }消息时，会执行source中的代码
 * connect: 会收到cvs和一个port，port对应一个worker。
 *          说明，这个worker将和本worker共享cvs里面的变量
 *          所以，我们需要将每个cvs中的key，都加入到map中。
 */
self.onmessage = (event) => {
    let data = event.data
    let command = data.command;
    switch (command) {
        case 'init': 
            let id = data.id;
            workerId = id;
            workerName = `worker:${workerId}`;
            Logger.info(`initialize a worker:${workerId}`);
            break;
        case 'start':
            const func = new Function(data.source);
            try {
                Logger.info(`${workerName} task processed...`);
                func();
                Logger.info(`${workerName} task finished... but worker still running`);
            } catch (error) {
                Logger.error(`${workerName} error: ${error}`);
            }
            break;
        // connect a channel from another worker
        // cvs 是与该worker共享的相同的变量的集合
        // 此事件会发生多次，在worker存在的时候
        case 'connect':
            let workerPort = event.ports[0];
            let cvs: Set<string> = data.cvs;

            // 将每个cv名字（key）都加入到map中
            cvs.forEach(cv => {
                if (!keyToPort.has(cv)) {
                    keyToPort.set(cv, new Set());
                }
                keyToPort.get(cv)!.add(workerPort);
            });

            workerPort.onmessage = (ev) => {
                let data = ev.data;
                let command = data.command;
                if (command === 'update') {
                    objectMap.set(data.key, data.value);
                    Logger.info(`${workerName} receive a update of ` + data.key + ':' + data.value);
                }
            };
            break;
        default:
            Logger.warn('Received unknown command:' + event.data.command);
    }
};
