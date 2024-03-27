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
	objects.set(key, value);
}

/**
 * sync: {
 * 		command: 'sync',
 * 		key: key,
 * 		lock: lock, (a sharedArrayBuffer) 
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

			const key = e.data.key;
			const arr = e.data.arr;
			const obj = get(key);
			serialize(obj, arr);
			Atomics.notify(arr, 0);
			break;
		}
		case 'update': {
			let key = e.data.key;
			let value = e.data.value;
			let lock = e.data.lock;
			set(key, value);
			Atomics.store(lock, 0, 1)
			Atomics.notify(lock, 0);
			break;
		}
	}
}
// 锁的阻塞队列
const waitingLockQueues = new Map();
// 锁的持有者
const lockHolders = new Map();
// 锁的等待队列
const waitingQueues = new Map();

function sync(data) {
	let key = data.key;
	//有线程持有锁
	if (lockHolders.get(key)) {
		//不是同一线程，加入阻塞队列
		if (lockHolders.get(key).workerId !== data.workerId) {
			if (!waitingLockQueues.has(key)) {
				waitingLockQueues.set(key, []);
			}
			waitingLockQueues.get(key).push(data);
		} else {
			//是同一线程
			lockHolders.get(key).count += 1;
			Atomics.store(data.lock, 0, 1);
			Atomics.notify(data.lock, 0);
		}
	} else {
		//没线程持有锁
		lockHolders.set(key, data);
		Atomics.store(data.lock, 0, 1);
		Atomics.notify(data.lock, 0);
	}
}
function wait(data) {
	let key = data.key;
	//获取线程持有的锁的数量
	data.count = lockHolders.get(key).count;
	//加入到等待队列
	if (!waitingQueues.has(key)) {
		waitingQueues.set(key, []);
	}
	waitingQueues.get(key).push(data);
	//释放锁
	lockHolders.delete(key);
	//分配锁
	let set = waitingLockQueues.get(key)
	const d = set?.shift();
	if (!d) {
		return;
	}
	lockHolders.set(key, d);
	Atomics.store(d.lock, 0, 1);
	Atomics.notify(d.lock, 0);
}

function exitSync(data) {
	let key = data.key;
	if (lockHolders.get(key)) {
		//持有锁的数量减1
		lockHolders.get(key).count -= 1;
		//判断是否释放锁
		if (lockHolders.get(key).count === 0) {
			lockHolders.delete(key);
			//分配锁
			let set = waitingLockQueues.get(key)
			const d = set.shift();
			if (!d) {
				return;
			}
			lockHolders.set(key, d);
			Atomics.store(d.lock, 0, 1);
			Atomics.notify(d.lock, 0);
		}
	}

}
function notify(data) {
	let key = data.key;
	if (waitingQueues.has(key)) {
		let set = waitingQueues.get(key);
		const d = set.shift();
		if (d) {
			if (!waitingLockQueues.has(key)) {
				waitingLockQueues.set(key, []);
			}
			waitingLockQueues.get(key).push(d)
		}
	}
}

function notifyAll(data) {
	let key = data.key;
	if (waitingQueues.has(key)) {
		let set = waitingQueues.get(key);
		if (set) {
			if (!waitingLockQueues.has(key)) {
				waitingLockQueues.set(key, []);
			}
			for (let element of set) {
				waitingLockQueues.get(key).push(element);
			}
		}
	}
}


function serialize(obj, buf) {
	const value = {
		value: obj
	}
	const jsonStr = JSON.stringify(value);
	const arr = new TextEncoder().encode(jsonStr);
	const size = arr.byteLength;

	buf[0] = size;
	buf.set(arr, 1);
}