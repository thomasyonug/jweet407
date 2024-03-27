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
		this.worker.onmessage = onmessage;
		// assign the id to every worker
		this.workerId = WebWorker.nextId();
		this.worker.postMessage({ 'command': 'init', 'id': `${this.workerId}` });
		// register the channel of variable in capturedCVS
		// for (let key in this.__captured_cvs) {
		// 	this.cvsSet.add(key);
		// }
	}
	// 注册完成后，启动worker
	start() {
		this.init();
		this.worker.postMessage({ 'command': 'start', 'source': this.source });
	}
}

const objects = new Map();
function get(key) {
	return objects.get(key);
}
function set(key, value) {
	objects.set(key,value);
}

/**
 * sync: {
 * 		command: 'sync',
 * 		key: key,
 * 		lock: lock, (a sharedArrayBuffer) 
 * }
 */
function onmessage (e) {
	const data = e.data;
	const command = data.command;
	switch (command) {
		// require a lock
		case 'sync':
			pushWaitingLock(e.data);
			lockDispatch(e.data);
			break;
		case 'wait':
			stopRunning(e.data);
			pushWaiting(e.data);
			lockDispatch(e.data);
			break;
		case 'unsync':
			stopRunning(e.data);
			lockDispatch(e.data);
			break;
		case 'notify':
			moveWaitingToWaitingLock(e.data);
			lockDispatch(e.data);
			break;
		case 'notifyAll':
			while (waitingQueues.size > 0) {
				moveWaitingToWaitingLock(e.data);
			}
			lockDispatch(e.data);
			break;
		case 'query': {
			// console.log('query');
			//const key = e.data.key;
			const arr = e.data.arr;
			//const obj = get(key);
			// TODO: handle null value of obj
			console.log('主线程收到get后发送：');
			console.log(objects);
			serialize(objects, arr);
			Atomics.notify(arr, 0);
			break;
		}
		case 'update': {
			let changedObj = e.data.obj;
			changedObj.forEach((value, key) => {
				set(key, value);
			});
			console.log('主线程收得到set后的objects:');
			console.log(objects);
			let lock = e.data.lock;
			//set(key, value);
			Atomics.store(lock, 0, 1)
			Atomics.notify(lock, 0);
			break;
		}
	}
}

function serialize(obj, arr) {
	const serializedObjects = JSON.stringify(Array.from(obj)); // 将 Map 转换为数组再序列化
	const uint8Array = new Uint8Array(arr.buffer);
	for (let i = 0; i < serializedObjects.length; i++) {
		uint8Array[i] = serializedObjects.charCodeAt(i);
	}
}

const waitingLockQueues = new Map();
const lockHolders = new Map();
const waitingQueues = new Map();

// 将一个进程放入等待队列
function pushWaitingLock(data) {
	let key = data.key;
	// let lock = data.lock;
	if (!waitingLockQueues.has(key)) {
		waitingLockQueues.set(key, []);
	}
	waitingLockQueues.get(key).push(data);
}

function pushWaiting(data) {
	let key = data.key;
	if (!waitingQueues.has(key)) {
		waitingQueues.set(key, []);
	}
	waitingQueues.get(key).push(data);
}

function moveWaitingToWaitingLock(data) {
	let key = data.key;
	if (!waitingQueues.has(key)) {
		return;
	}
	let set = waitingQueues.get(key);
	const d = set.shift();
	if (!d) {
		return;
	}
	if (!waitingLockQueues.has(key)) {
		waitingLockQueues.set(key, []);
	}
	waitingLockQueues.get(key).push(d);
}

function lockDispatch(data) {
	let key = data.key;
	if (lockHolders.get(key)) {
		return;
	}
	let set = waitingLockQueues.get(key)
	const d = set?.shift();
	if (!d) {
		return;
	}
	lockHolders.set(key, d);
	Atomics.store(d.lock, 0, 1);
	Atomics.notify(d.lock, 0);
}

function stopRunning(data) {
	let key = data.key;
	lockHolders.delete(key);
}