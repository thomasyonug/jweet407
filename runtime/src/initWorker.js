/**
 * initWorker
 * 一个简单的worker，是每个worker启动的初始状态
 */
// cvs更新后的值会在这里
let workerId;
let workerName;
let __key;
const BUF_SIZE = 16;
const USE_OPTIMIZE = true;
//Atomics.wait的时间单位为毫秒，定义其他时间单位到毫秒的倍数
class TimeUnit {
	static DAYS = 24 * 60 * 60 * 1000;
	static HOURS = 60 * 60 * 1000;
	static MINUTES = 60 * 1000;
	static SECONDS = 1000;
	static MILLISECONDS = 1;
	static MICROSECONDS = 1e-3;
	static NANOSECONDS = 1e-6;
}
function createLock() {
	const lock = new Int32Array(new SharedArrayBuffer(8));
	return lock;
}
//map存储所有改变了的key value
let changedObjects = new Map();
//map存储所有的主线程中的key value
let mainObject = new Map();
class lock {
	__key;
	lock() {
		Comm.synchronizePostMessage({ 'command': 'sync', 'key': this.__key, "workerId": workerId });
		if (USE_OPTIMIZE) {
			Comm.batch_query();
		}
	}
	unlock() {
		if (USE_OPTIMIZE) {
			Comm.batch_update(changedObjects);
		}
		postMessage({ 'command': 'unsync', 'key': this.__key, "workerId": workerId });
	}
	tryLock(time, timeUnit) {
		if (arguments.length == 0) {
			if (Comm.synchronizePostMessageWithReturn({ 'command': 'tryLock', 'key': this.__key, "workerId": workerId })) {
				Comm.batch_query();
				return true;
			} else {
				return false;
			}
		}
		else {
			let waitTime = time * timeUnit;
			
			const lock = createLock();
			postMessage({ 'command': 'sync', 'key': this.__key, 'lock': lock, "workerId": workerId });
			if (Atomics.wait(lock, 0, 0, waitTime) === "timed-out") {
				Atomics.store(lock, 0, 2);
				return false;
			}
			Comm.batch_query();
			return true;
		}
	}
	newCondition() {
		let condition = new Condition()
		condition.__lockName = this.__key;
		return condition;
	}
}
class ReentrantLock extends lock {

}

class Condition {
	await() {
		Comm.batch_update(changedObjects);
		Comm.synchronizePostMessage({ 'command': 'await', 'key': this.__key, 'lockName': this.__lockName, "workerId": workerId });
	}
	signal() {
		postMessage({ 'command': 'signal', 'key': this.__key, 'lockName': this.__lockName, "workerId": workerId });
	}
	signalAll() {
		postMessage({ 'command': 'signalAll', 'key': this.__key, 'lockName': this.__lockName, "workerId": workerId });
	}
}
class readLock extends lock {
	lock() {

		Comm.synchronizePostMessage({ 'command': 'readLock.lock', 'key': this.__lockName, "workerId": workerId });
		Comm.batch_query();
	}
	unlock() {
		Comm.batch_update(changedObjects);
		postMessage({ 'command': 'readLock.unlock', 'key': this.__lockName, "workerId": workerId });
	}
	tryLock(time, timeUnit) {

		if (arguments.length == 0) {
			if (Comm.synchronizePostMessageWithReturn({ 'command': 'readLock.tryLock', 'key': this.__lockName, "workerId": workerId })) {
				Comm.batch_query();
				return true;
			} else {
				return false;
			}
		}
		else {
			let waitTime = time * timeUnit;
			//console.log(waitTime);
			const lock = createLock();
			postMessage({ 'command': 'sync', 'key': this.__lockName, 'lock': lock, "workerId": workerId });
			if (Atomics.wait(lock, 0, 0, waitTime) === "timed-out") {
				Atomics.store(lock, 0, 2);
				return false;
			}
			return true;
		}
	}
	newCondition() {
		let condition = new ReadWriteCondition()
		condition.__lockName = this.__lockName;
		return condition;
	}
}
class writeLock {

	lock() {
		Comm.synchronizePostMessage({ 'command': 'writeLock.lock', 'key': this.__lockName, "workerId": workerId });
		Comm.batch_query();
	}
	unlock() {
		Comm.batch_update(changedObjects);
		postMessage({ 'command': 'writeLock.unlock', 'key': this.__lockName, "workerId": workerId });
	}
	tryLock(time, timeUnit) {

		if (arguments.length == 0) {
			return Comm.synchronizePostMessageWithReturn({ 'command': 'writeLock.tryLock', 'key': this.__lockName, "workerId": workerId })
		}
		else {
			let waitTime = time * timeUnit;
			//console.log(waitTime);
			const lock = createLock();
			postMessage({ 'command': 'sync', 'key': this.__lockName, 'lock': lock, "workerId": workerId });
			if (Atomics.wait(lock, 0, 0, waitTime) === "timed-out") {
				Atomics.store(lock, 0, 2);
				return false;
			}
			return true;
		}
	}
	newCondition() {
		let condition = new Condition()
		condition.__lockName = this.__lockName;
		return condition;
	}
}
class ReentrantReadWriteLock {
	__key;
	rLock = new readLock();
	wLock = new writeLock();
	readLock() {
		this.rLock.__lockName = this.__key;
		this.rLock.__type = "Read";
		return this.rLock;
	}
	writeLock() {
		this.wLock.__lockName = this.__key;
		this.rLock.__type = "Write";
		return this.wLock;
	}
}

class StampedLock {
	readLock() {
		let l = this.syncPostMessage({ 'command': 'readLock', 'key': this.__key, "workerId": workerId });
		Comm.batch_query();
		return l;
	}
	unlockRead(stamp) {
		postMessage({ 'command': 'unlockRead', 'key': this.__key, 'stamp': stamp, "workerId": workerId });
	}
	writeLock() {
		let l = this.syncPostMessage({ 'command': 'writeLock', 'key': this.__key, "workerId": workerId });
		Comm.batch_query();
		return l;
	}
	unlockWrite(stamp) {
		Comm.batch_update(changedObjects);
		postMessage({ 'command': 'unlockWrite', 'key': this.__key, 'stamp': stamp, "workerId": workerId });

	}
	tryReadLock(time, timeUnit) {
		//Todo
		if (arguments.length == 0) {
			let l = this.syncPostMessage({ 'command': 'tryReadLock', 'key': this.__key, "workerId": workerId });
			if (l) {
				Comm.batch_query();
			}
			return l;
		} else {
			let waitTime = time * timeUnit;

			const lock = createLock();
			postMessage({ 'command': 'readLock', 'key': this.__key, 'lock': lock, "workerId": workerId });
			if (Atomics.wait(lock, 0, 0, waitTime) === "timed-out") {
				Atomics.store(lock, 0, 2);
				return 0;
			}
			return Atomics.load(lock, 1);
		}




	}
	tryWriteLock(time, timeUnit) {
		//Todo
		if (arguments.length == 0) {
			let l = this.syncPostMessage({ 'command': 'tryWriteLock', 'key': this.__key, "workerId": workerId });
			if (l) {
				Comm.batch_query();
			}
			return l;
		} else {
			let waitTime = time * timeUnit;

			const lock = createLock();
			postMessage({ 'command': 'writeLock', 'key': this.__key, 'lock': lock, "workerId": workerId });
			if (Atomics.wait(lock, 0, 0, waitTime) === "timed-out") {
				Atomics.store(lock, 0, 2);
				return 0;
			}
			return Atomics.load(lock, 1);
		}
	}
	tryOptimisticRead() {
		let l = this.syncPostMessage({ 'command': 'tryOptimisticRead', 'key': this.__key, "workerId": workerId });
		Comm.batch_query();
		return l;
	}
	validate(stamp) {
		return this.syncPostMessage({ 'command': 'validate', 'key': this.__key, 'stamp': stamp, "workerId": workerId });
	}
	tryConvertToWriteLock(stamp) {

		return this.syncPostMessage({ 'command': 'tryConvertToWriteLock', 'key': this.__key, 'stamp': stamp, "workerId": workerId });
	}
	tryConvertToReadLock(stamp) {

		return this.syncPostMessage({ 'command': 'tryConvertToReadLock', 'key': this.__key, 'stamp': stamp, "workerId": workerId });
	}
	syncPostMessage(message) {
		const lock = createLock();
		postMessage({ ...message, lock });
		Atomics.wait(lock, 0, 0);
		return Atomics.load(lock, 1);
	}

}
class Comm {
	static sync(obj) {
		const key = obj.__key;
		this.synchronizePostMessage({ 'command': 'sync', 'key': key, "workerId": workerId });
		if (USE_OPTIMIZE) {
			Comm.batch_query();
		}
	}
	static unsync(obj) {
		if (USE_OPTIMIZE) {
			Comm.batch_update(changedObjects);
		}
		const key = obj.__key;
		postMessage({ 'command': 'unsync', 'key': key, "workerId": workerId });
	}
	static wait(obj) {
		if (USE_OPTIMIZE) {
			Comm.batch_update(changedObjects);
		}
		const key = obj.__key;
		this.synchronizePostMessage({ 'command': 'wait', 'key': key, "workerId": workerId });
		if (USE_OPTIMIZE) {
			Comm.batch_query();
		}
	}
	static notify(obj) {
		const key = obj.__key;
		postMessage({ 'command': 'notifyAll', 'key': key, "workerId": workerId });
	}
	static update(key, value) {
		const lock = createLock();
		postMessage({ 'command': 'update', key, value, lock });
		Atomics.wait(lock, 0, 0);
	}
	static batch_update(changedObjs) {
		if (!changedObjs || changedObjs.size === 0) return;
		Logger.info('子线程一次性set:');
		//Logger.info(changedObj);
		this.synchronizePostMessage({ 'command': 'batch_update', 'obj': changedObjs });
		changedObjs.clear();
	}
	static query(key) {
		const buffer = new SharedArrayBuffer(256);
		const arr = new Int32Array(buffer);
		postMessage({ 'command': 'query', 'key': key, 'arr':arr });
		Atomics.wait(arr, 0, 0);
		return deserialize(arr);
	}
	static batch_query() {
		const buffer = new SharedArrayBuffer(BUF_SIZE * 2);
		const arr = new Int32Array(buffer);
		let _objects = this.synchronizePostMessageWithData(arr);
		mainObject = new Map([...mainObject, ..._objects]);
	}
	static synchronizePostMessageWithData(arr) { // Int32Array
		postMessage({ 'command': 'batch_query', 'arr': arr });
		Atomics.wait(arr, 0, 0);
		let str = '';
		str += deserialize2MapStr(arr);
		// 如果还有后续的数据传送，要继续接收
		let flag = Atomics.load(arr, 0);
		while (flag !== -1) {
			//Logger.debug(str);
			Atomics.store(arr, 0, 0);
			// Atomics.wait(arr, 0, 0); // 不能wait，因为store和wait不是原子性，有可能store往还没wait，对方就notify了，导致wait永远等待
			while (Atomics.load(arr, 0) === 0) { }
			flag = Atomics.load(arr, 0);
			str += deserialize2MapStr(arr);
		}
		let objects = deserialize2Map(str);
		return objects;
	}

	// 发送一个消息，然后等待对面处理后再返回，不需要返回值
	static synchronizePostMessage(message) {
		const lock = createLock();
		postMessage({ ...message, lock });
		Atomics.wait(lock, 0, 0);
	}
	static synchronizePostMessageWithReturn(message) {
		const lock = createLock();
		postMessage({ ...message, lock });
		Atomics.wait(lock, 0, 0);
		if (Atomics.load(lock, 0) === 2) {
			
			return false;
		}
		return true;
	}
	// join a worker by id
	static join(id) { 
		this.synchronizePostMessage({ 'command': 'join', 'workerId': id });
	}
	// tell main thread that this worker is finish processing
	static end() {
		postMessage({ 'command': 'end', 'workerId': workerId });
		Logger.info(`${workerName} task finished...`);
	}
}

/**
 * 从一个Int32Array反序列化为Map
 * @param {Int32Array} buf 
 * @returns {Map}
 */
function deserialize2Map(str) {
	const arr = JSON.parse(str); // 反序列化为数组
	return new Map(arr); // 从数组创建 Map
}
function deserialize2MapStr(buf) {
	const uint8Array = new Uint8Array(buf.buffer);
	let serializedObjects = '';
	for (let i = 4; i < uint8Array.length && uint8Array[i] !== 0; i++) {
		serializedObjects += String.fromCharCode(uint8Array[i]);
	}
	return serializedObjects;
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
let buildProxy = (target, prefix = "") => {
	// get the name of the class
	let className;
	if (prefix === "") {
		className = target.prototype.constructor.name;
	} else {
		className = prefix;
	}
	// trim the redundant "__" in the class name
	className = className.replace(/^__+/, '');
	// create a new object in the container
	return new Proxy(target, {
		get: function (_target, propKey) {
			let key = className + '.' + propKey;
			//console.log("get: " + key)
			// 如果是对象，递归创建代理
			if (_target[propKey] instanceof Object && propKey != 'prototype') {
				return buildProxy(_target[propKey], key);
			}
			if (!USE_OPTIMIZE) {
				const value = Comm.query(key);
				return value==null?_target[propKey]:value;
			}

			if (target.__captured_volatile_cvs != null && Object.keys(target.__captured_volatile_cvs).includes(propKey)) {
				var tmp = Comm.query(key);
				changedObjects.set(key, tmp);
				if (tmp == null) {
					return _target[propKey];
				} else {
					return tmp;
				}
			}
			if (mainObject.has(key)) {
                return mainObject.get(key);
            }
			return _target[propKey];
		},
		set: function (_target, propKey, newValue) {
			let key = className + '.' + propKey;
			if (newValue instanceof Object) {
				newValue.__key = key;
			}
			if (!USE_OPTIMIZE) {
				Comm.update(key, newValue);
				return true;
			}
			_target[propKey] = newValue;
			mainObject.set(key, newValue)
			//锁Object的时候不需要更新,否则序列化反序列化的时候会出错
			if (!(newValue instanceof Object)) { changedObjects.set(key, newValue); }
			//判断是否是volatile变量，若是，立马更新
			if (target.__captured_volatile_cvs != null && Object.keys(target.__captured_volatile_cvs).includes(propKey)) {
//				console.info("Volatile update now!")
				Comm.update(key, newValue);
			}
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
			__key = data.key;
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
			Comm.end();
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

const java = {
    lang: {
        Thread: class Thread {
            start() {
                this.run()
            }
			static sleep(timeOut) {
				const start = Date.now();
				const end = start + timeOut;
				while (Date.now() < end) { }
			}
            constructor(obj) {
			    this.__key = __key;
                if (obj) {
                    obj.__key = Math.random();
                    return obj;
                }
                this.workerId = workerId
            }
            join() {
                Comm.join(this.workerId)
            }
        }
	},
	// java.util.concurrent.locks
	util: {
		concurrent: {
			locks: {
				ReentrantLock,
				ReentrantReadWriteLock,
				StampedLock
			}
		}
	}
}

Object.prototype.wait = function () {
	Comm.wait(this)
}

Object.prototype.notify = function () {
	Comm.notify(this)
}

Object.prototype.notifyAll = function () {
	Comm.notify(this)
}
