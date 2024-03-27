/**
 * initWorker
 * 一个简单的worker，是每个worker启动的初始状态
 */
/**
 * 一系列全局变量，在worker的整个生命周期中都可以被调用
 */
// ============== Start of 全局变量 ==============
// cvs更新后的值会在这里
let workerId;
let workerName;

const lockByKey = new Map();//根据不同key创建不同锁，同一个key之创建一次锁

function createLock(key) {
	if(lockByKey.get(key)){
		lock = lockByKey.get(key);
		Atomics.store(lock,0,0);
		return lock;
	}
	lock = new Int32Array(new SharedArrayBuffer(4));
	lockByKey.set(key,lock);
	return lock;
}

class Comm {
	// 锁
	static sync(obj) {
		const key = obj.__key;
		
		const lock = createLock(key);
		postMessage({ 'command': 'sync', 'key': key, 'lock': lock ,"workerId":workerId})
		Atomics.wait(lock, 0, 0);
	}
	static unsync(obj) {
		const key = obj.__key;
		postMessage({ 'command': 'unsync', 'key': key,"workerId":workerId});
	}
	static wait(obj) {
		const key = obj.__key;
		const lock = createLock(key);
		postMessage({ 'command': 'wait', 'key': key, lock ,"workerId":workerId});
		Atomics.wait(lock, 0, 0);
	}
	static notify(obj) {
		const key = obj.__key;
		postMessage({ 'command': 'notify', 'key': key,"workerId":workerId});
	}
	// data
	static update(key, value) {
		const lock = createLock(key);
		postMessage({ 'command': 'update', key, value, lock });
		Atomics.wait(lock, 0, 0);
	}
	static query(key) {
		const buffer = new SharedArrayBuffer(256);
		const arr = new Int32Array(buffer);
		postMessage({ 'command': 'query', 'key': key, 'arr':arr });
		Atomics.wait(arr, 0, 0);
		return deserialize(arr);
	}
	static synchronizePostMessage() {

	}
}

function deserialize(buf) {
	const arr = new Uint8Array(buf);
	const jsonStr = new TextDecoder().decode(arr.slice(1, arr[0] + 1));
	const obj = JSON.parse(jsonStr);
	return obj.value;
}

/**
 * return a proxyHandler associated with the target
 * @param {any} target 
 */
let buildProxy = (target) => {
	// get the name of the class
	let className = target.prototype.constructor.name;
	// trim the redundant "__" in the class name
	className = className.replace(/^__+/, '');
	// create a new object in the container
	return new Proxy(target, {
		get: function (_target, propKey) {
			let key = className + '.' + propKey;
			const value = Comm.query(key);
			return value==null?_target[propKey]:value;
		},
		set: function (clz, propKey, newValue) {
			let key = className + '.' + propKey;
			if (newValue instanceof Object) {
				newValue.__key = key;
			}
			Comm.update(key, newValue);
			return true;
		}
	});
}
// ============== End of 全局变量 ================
/**
 * start:   当接受到{ command: 'start', source }消息时，会执行source中的代码
 * connect: 会收到cvs和一个port，port对应一个worker。
 *          说明，这个worker将和本worker共享cvs里面的变量
 *          所以，我们需要将每个cvs中的key，都加入到map中。
 */
self.onmessage = (event) => {
	let data = event.data;
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
			}
			catch (error) {
				Logger.error(`${workerName} error: ${error}`);
			}
			break;
		default:
			Logger.warn('Received unknown command:' + event.data.command);
	}
};

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