/**
 * initWorker
 * 一个简单的worker，是每个worker启动的初始状态
 */
// cvs更新后的值会在这里
let workerId;
let workerName;

const lockByKey = new Map(); //根据不同key创建不同锁，同一个key只创建一次锁

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
		Comm.query();
	}
	static unsync(obj) {
		Comm.update(changedObjects);
		const key = obj.__key;
		postMessage({ 'command': 'unsync', 'key': key,"workerId":workerId});
	}
	static wait(obj) {
		Comm.update(changedObjects);
		const key = obj.__key;
		const lock = createLock(key);
		postMessage({ 'command': 'wait', 'key': key, lock ,"workerId":workerId});
		Atomics.wait(lock, 0, 0);
	}
	static notify(obj) {
		const key = obj.__key;
		postMessage({ 'command': 'notify', 'key': key,"workerId":workerId});
	}
	static update(changedObj) {
		if(changedObj.size===0) return;
		const lock = createLock();
		Logger.info('子线程一次性set:' );
		Logger.info(changedObj);
		postMessage({ 'command': 'update', 'obj':changedObj, lock });
		Atomics.wait(lock, 0, 0);
		changedObj.clear();
	}
	static query() {
		const buffer = new SharedArrayBuffer(256);
		const arr = new Int32Array(buffer);
		postMessage({ 'command': 'query',  'arr':arr });

		Atomics.wait(arr, 0, 0);Logger.info('============');
		let _objects= deserialize(arr);
		Logger.info('子线程一次性get:' );
		Logger.info(_objects);
		mainObject = new Map([...mainObject, ..._objects]);
	}
	static synchronizePostMessage() {

	}
}

function deserialize(arr) {
	const uint8Array = new Uint8Array(arr.buffer);
	let serializedObjects = '';
	for (let i = 0; i < uint8Array.length && uint8Array[i] !== 0; i++) {
		serializedObjects += String.fromCharCode(uint8Array[i]);
	}
	Logger.info(serializedObjects);
	Logger.info('子线程收到get后反序列化：');
	const _objectsArray = JSON.parse(serializedObjects); // 反序列化为数组
	Logger.info(_objectsArray);
	return  new Map(_objectsArray); // 从数组创建 Map
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
			if(mainObject.has(key)) {
				return mainObject.get(key);
			}
			return _target[propKey];
		},
		set: function (clz, propKey, newValue) {
			let key = className + '.' + propKey;
			if (newValue instanceof Object) {
				newValue.__key = key;
			}
			clz[propKey] = newValue;
			mainObject[key] = newValue;
			//锁Object的时候不需要更新,否则序列化反序列化的时候会出错
			if(!(newValue instanceof Object)){changedObjects.set(key, newValue);}
			return true;
		}
	});
}
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

//map存储所有改变了的key value
let changedObjects = new Map();
//map存储所有的主线程中的key value
let mainObject = new Map();

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