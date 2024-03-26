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
	e.data.count = 1;
	switch (command) {
		// require a lock
		case 'sync':
			// pushWaitingLock(e.data);
			// lockDispatch(e.data,0);
			sync(e.data);
			
			break;
		case 'wait':
			//stopRunning(e.data);
			wait(e.data);
			//pushWaiting(e.data);
			//lockDispatch(e.data,1);

			break;
		case 'unsync':
			exitSync(e.data);
			//lockDispatch(e.data);

			break;
		case 'notify':
			// moveWaitingToWaitingLock(e.data);
			// lockDispatch(e.data,1);
			notify(e.data)

			break;
		case 'notifyAll':
			moveAllWaitingToWaitingLock(e.data);
			//lockDispatch(e.data);
			break;
		case 'query': {

			const key = e.data.key;
			const arr = e.data.arr;
			const obj = get(key);
			// TODO: handle null value of obj
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
const waitingLockQueues = new Map();
const lockHolders = new Map();
const waitingQueues = new Map();
function sync(data){
	let key = data.key;
	//有线程持有锁
	if (lockHolders.get(key)) {
		//不是同一线程，加入阻塞队列
		if(lockHolders.get(key).workerId !== data.workerId ) {
			if (!waitingLockQueues.has(key)) {
				waitingLockQueues.set(key, []);
			}
			waitingLockQueues.get(key).push(data);

		}else{
			//是同一线程
			lockHolders.get(key).count += 1;
			Atomics.store(data.lock, 0, 1);
			Atomics.notify(data.lock, 0);		
		}
	}else{
		//没线程持有锁
		lockHolders.set(key, data);
		Atomics.store(d.lock, 0, 1);
		Atomics.notify(d.lock, 0);
	}
	

}
function wait(data) {
	let key = data.key;
	
	data.count = lockHolders.get(key).count;
	if (!waitingQueues.has(key)) {
		waitingQueues.set(key, []);
	}
	waitingQueues.get(key).push(data);
	lockHolders.delete(key);

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
		lockHolders.get(key).count -= 1;
		if (lockHolders.get(key).count === 0) {
			
			lockHolders.delete(key);
			let set = waitingLockQueues.get(key)
			const d = set?.shift();
			if (!d) {
				return;
			}
			
			lockHolders.set(key, d);
			Atomics.store(d.lock, 0, 1);
			Atomics.notify(d.lock, 0);
		}
	}

}
function notify(data){
	let key = data.key;
	if (waitingQueues.has(key)) {
		let set = waitingQueues.get(key);
		if(set){
			if (!waitingLockQueues.has(key)) {
				waitingLockQueues.set(key, []);
			}
			for(let element of set){
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
const threadCountMap = new Map();


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
function moveAllWaitingToWaitingLock(data) {
	let key = data.key;
	if (!waitingQueues.has(key)) {
		return;
	}
	let set = waitingQueues.get(key);
	
	if (!set) {
		return;
	}
	if (!waitingLockQueues.has(key)) {
		waitingLockQueues.set(key, []);
	}
	for(let element of set){
		waitingLockQueues.get(key).push(element);
	}
	
	
}

function lockDispatch(data,flag) {
	let key = data.key;
	if (lockHolders.get(key)&& flag !==1) {
		if(lockHolders.get(key).workerId !== data.workerId || lockHolders.get(key).key !== data.key){
			return;
		}else{
			lockHolders.get(key).count += 1;
			Atomics.store(lockHolders.get(key).lock, 0, 1);
			Atomics.notify(lockHolders.get(key).lock, 0);
		}

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
	pushWaiting(lockHolders.get(key));
	lockHolders.delete(key);
}