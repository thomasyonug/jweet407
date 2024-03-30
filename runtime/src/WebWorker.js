const BUF_SIZE = 8;
class WebWorker {
	worker = null;
	cvsSet = new Set();
	workerId = null;
	static workerCounter = 0;
	static nextId() {
		WebWorker.workerCounter += 1;
		return WebWorker.workerCounter;
	}
	static nameToWorker = new Map();
	name;
	constructor(name) {
		this.name = name;
	}

	init() {
		if (this.worker)
			return;
		// initialize worker
		this.worker = new Worker('./initWorker.js');
		WebWorker.nameToWorker.set(this.name, this.worker);
		this.worker.onmessage = onmessage;
		// assign the id to every worker
		this.workerId = WebWorker.nextId();
		this.worker.postMessage({ 'command': 'init', 'id': `${this.workerId}` });
	}
	// 注册完成后，启动worker
	start() {
		this.init();
		this.worker.postMessage({ 'command': 'start', 'source': this.source });
	}

}

const objects = new Map();
function getObject(key) {
	return objects.get(key);
}
function setObject(key, value) {
	objects.set(key, value);
}

/**
 * sync: {
 * 		command: 'sync',
 * 		key: key,
 * 		lock: lock, {Int32Array}
 * }
 */
function onmessage(e) {
	const data = e.data;
	const command = data.command;
	e.data.count = 1;
	switch (command) {
		// require a lock
		case 'sync':
			sync(e.data);
			break;
		case 'wait':
			wait(e.data);
			break;
		case 'unsync':
			exitSync(e.data);
			break;
		case 'notify':
			notify(e.data)
			break;
		case 'notifyAll':
			moveAllWaitingToWaitingLock(e.data);
			break;
		case 'query': {
			let arr = e.data.arr;
			responseWithData(arr);
			break;
		}
		case 'update': {
			let changedObj = e.data.obj;
			changedObj.forEach((value, key) => {
				setObject(key, value);
			});
			let lock = e.data.lock;
			Atomics.store(lock, 0, 1)
			Atomics.notify(lock, 0);
			break;
		}
		case 'join':{
			let receiver = e.data.receiver;
			let arr = e.data.arr;
			WebWorker.nameToWorker.get(receiver).postMessage({'command': 'join', 'arr': arr});
			break;
		}
	}
}

// 锁的阻塞队列
const blockQueues = new Map();
// 锁的持有者
const lockHolders = new Map();
// 锁的等待队列
const waitingQueues = new Map();

/**
 * 工具函数：
 */
function joinBlockQueue(key, data) {
	if (!blockQueues.has(key)) {
		blockQueues.set(key, []);
	}
	blockQueues.get(key).push(data);
}
function joinWaitingQueue(key, data) {
	if (!waitingQueues.has(key)) {
		waitingQueues.set(key, []);
	}
	waitingQueues.get(key).push(data);
}
function releaseLock(lock) {
	Atomics.store(lock, 0, 1);
	Atomics.notify(lock, 0);
}
function dispatchLock(key) {
	let set = blockQueues.get(key)
	const d = set?.shift();
	if (!d) {
		return;
	}
	lockHolders.set(key, d);
	releaseLock(d.lock);
}
function responseWithData(arr) {
	// arr 只有255 * 4 个字节，第一个字节存放flag，所以大于BUF_SIZE - 4个字节的Map需要分批序列化
	const JSONStr = JSON.stringify(Array.from(objects));
	const size = JSONStr.length;
	let remainder = size;
	const batch = Math.ceil(size / (BUF_SIZE - 4));
	for (let i = 0; i < batch; i++) {
		populateArray(arr, JSONStr, i * (BUF_SIZE - 4), BUF_SIZE - 4);
		if (i === batch - 1) {
			Atomics.store(arr, 0, -1);
			Atomics.notify(arr, 0);
		} else {
			Atomics.store(arr, 0, size);
			Atomics.notify(arr, 0);
			while (Atomics.load(arr, 0) !== 0) { }
		}
	}
}

/**
 * sync要做的操作是：
 * 1. 如果有线程持有锁，判断是否是同一线程
 * 		1.1. 如果是同一线程，持有锁的数量加1
 *  	1.2. 如果不是同一线程，加入阻塞队列，等待获取锁
 */
function sync(data) {
	let key = data.key;
	if (lockHolders.get(key)) {
		if (lockHolders.get(key).workerId !== data.workerId) {
			joinBlockQueue(key, data);
		} else {
			lockHolders.get(key).count += 1;
			releaseLock(data.lock);
		}
	} else {
		lockHolders.set(key, data);
		releaseLock(data.lock);
	}
}
/**
 * exitSync要做的操作是：
 * 判断锁的数量是否为0，如果是0，释放该锁，重新分配锁
 */
function exitSync(data) {
	let key = data.key;
	if (lockHolders.get(key)) {
		//持有锁的数量减1
		lockHolders.get(key).count -= 1;
		//判断是否释放锁
		if (lockHolders.get(key).count === 0) {
			lockHolders.delete(key);
			dispatchLock(key);
		}
	}
}
/**
 * wait要做的操作是：
 * 释放锁，加入等待队列
 */
function wait(data) {
	let key = data.key;
	data.count = lockHolders.get(key).count; // 记录锁计数器
	joinWaitingQueue(key, data);
	lockHolders.delete(key);
	dispatchLock(key);
}

/**
 * notify要做的操作是：
 * 通知等待队列中的一个线程，加入锁的阻塞队列
 */
function notify(data) {
	let key = data.key;
	if (waitingQueues.has(key)) {
		let set = waitingQueues.get(key);
		const d = set.shift();
		if (d) {
			joinBlockQueue(key, d);
		}
	}
}
function notifyAll(data) {
	let key = data.key;
	if (waitingQueues.has(key)) {
		let set = waitingQueues.get(key);
		if (set) {
			if (!blockQueues.has(key)) {
				blockQueues.set(key, []);
			}
			for (let element of set) {
				blockQueues.get(key).push(element);
			}
		}
	}
}

/**
 * 序列化一个Map为Int32Array buffer
 * @param {*} arr 
 * @param {*} mapStr 
 * @param {*} start  start of the string
 * @param {*} len    
 */
function populateArray(arr, mapStr, start, len) {
	const uint8Array = new Uint8Array(arr.buffer);
	for (let i = 0; i < len && i + start < mapStr.length; i++) {
		uint8Array[i + 4] = mapStr.charCodeAt(start + i);
	}
	// set the last byte to 0
	const send = Math.min(mapStr.length - start, len);
	uint8Array[4 + send] = 0;
}