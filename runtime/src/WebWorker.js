const BUF_SIZE = 16;
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
	}
	// 注册完成后，启动worker
	start() {
		this.init();
		this.worker.postMessage({ 'command': 'start', 'source': `(${this.source.toString()})()` });
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
	e.data.type = 'Write';
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
			notifyAll(e.data);
			break;
		case 'await':
			await(e.data);
			break;
		case 'signal':
			signal(e.data);
			break;
		case 'signalAll':
			signalAll(e.data);
			break;
		case 'tryLock':{
			tryLock(e.data);
			break;
		}
		case 'readLock.tryLock':{
			e.data.type= 'Read';
			tryLock(e.data);
		}
		case 'writeLock.tryLock':{
			e.data.type='Write';
			tryLock(e.data);
		}
		case 'readLock.lock':
			e.data.type = 'Read';
			sync(e.data);
			break;
		case 'writeLock.lock':
			e.data.type = 'Write';
			sync(e.data);
			break;
		
		case 'readLock.unlock':{
			exitSync(e.data);
			break;
		}
		case 'writeLock.unlock':{
			exitSync(e.data);
			break;
		}
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
		case 'syncRW':
            syncRW(e.data);
            break;        
        case 'unsyncRW':
            exitSyncRW(e.data);
            break;
	}
}
// 锁的阻塞队列
const blockQueues = new Map();
// 锁的持有者
const lockHolders = new Map();
// 锁的等待队列
const waitingQueues = new Map();
//锁的条件等待队列
const conditionWaitingQueue = new Map();
/**
 * 工具函数：
 */
function joinBlockQueue(key, data) {
	if (!blockQueues.has(key)) {
		blockQueues.set(key, []);
	}
	blockQueues.get(key).push(data);
}
function deleteTimeoutLock(blockQueues,key){
	let set = blockQueues.get(key);
	if(!set){
		return ;
	}
	for(let i =set.length-1;i>=0;i--){
		if(Atomics.load(set[i].lock)===2){
			set.splice(i,1);
		}
	}
	
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
function failReleaseLock(lock){
	Atomics.store(lock, 0, 2);
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
	let type = data.type;
	let key = data.key;
	if(type === 'Write'){
		if(lockHolders.get(key)){
			if(lockHolders.get(key).workerId !== data.workerId || lockHolders.get(key).type === 'Read'){
				joinBlockQueue(key,data);
			}else{
				lockHolders.get(key).count += 1;
				releaseLock(data.lock);	

			}
		}else{
			lockHolders.set(key,data);
			releaseLock(data.lock);

		}
	}else{
		if(lockHolders.get(key)){
			if(lockHolders.get(key).workerId !== data.workerId && lockHolders.get(key).type === 'Write')	{
				joinBlockQueue(key,data);
			}else{
				lockHolders.get(key).count += 1;
				releaseLock(data.lock);	

			}	
		}else{
			lockHolders.set(key,data);
			releaseLock(data.lock);

		}
	
	}
	
}


/**
 * exitSync要做的操作是：
 * 判断锁的数量是否为0，如果是0，释放该锁，重新分配锁
 */
function exitSync(data) {
	let key = data.key;
	let type = lockHolders.get(key).type;
	if (lockHolders.get(key)) {
		//持有锁的数量减1
		lockHolders.get(key).count -= 1;
		//判断是否释放锁
		if (lockHolders.get(key).count === 0) {
			//删除超时请求
			deleteTimeoutLock(blockQueues,key);
			lockHolders.delete(key);
			//取出阻塞队列的第一个元素
			let dataList = blockQueues.get(key);
			if(!dataList){
				return;
			}
			let first = dataList.shift();
			if(!first){
				return;
			}
			releaseLock(first.lock);
			lockHolders.set(key, first);
			//如果阻塞队列第一个是申请读锁，那么将所有读都释放
			if(first.type === 'Read'){
				//从后向前遍历，释放所有读锁
				for(let i = dataList.length - 1; i >= 0; i--){
					if(dataList[i].type === 'Read'){
						releaseLock(dataList[i].lock);
						lockHolders.get(key).count += 1;
						dataList.splice(i,1);
					}
				}
			}
				
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
function await(data){
	let condition = data.key;
	let lockName = data.lockName;
	data.count = lockHolders.get(lockName).count; // 记录锁计数器
	joinConditionWaitingQueue(condition,lockName,data);
	lockHolders.delete(lockName);
	dispatchLock(lockName);
}
function joinConditionWaitingQueue(condition,lockName,data){
	if(!conditionWaitingQueue.get(condition)){
		conditionWaitingQueue.set(condition,new Map())
	}
	let lockToData = conditionWaitingQueue.get(condition);
	if(!lockToData.get(lockName)){
		lockToData.set(lockName,[])
	}
	let dataList = lockToData.get(lockName);
	dataList.push(data);
	

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
function signal(data){
	let codition = data.key;
	let lockName = data.lockName;
	if(conditionWaitingQueue.has(codition)){
		let lockToData = conditionWaitingQueue.get(codition);
		if(lockToData.has(lockName)){
			let dataList = lockToData.get(lockName);
			let d = dataList.shift();
			if(d){
				joinBlockQueue(lockName,d);
			}
		}
	}
}
function signalAll(data){
	let condition = data.key;
	if(conditionWaitingQueue.has(condition)){
		let lockToData = conditionWaitingQueue.get(condition);
		lockToData.forEach((dataList,lockName) => {
			dataList.forEach(d => {
				joinBlockQueue(lockName,d);
			})
		})
	}
}
function tryLock(data){
	let key = data.key;
	let type =data.type;

	if(type === 'Write'){
		if(lockHolders.get(key)){
			if(lockHolders.get(key).workerId !== data.workerId || lockHolders.get(key).type === 'Read'){
				failReleaseLock(data.lock);
			}else{
				lockHolders.get(key).count += 1;
				releaseLock(data.lock);	

			}
		}else{
			lockHolders.set(key,data);
			releaseLock(data.lock);

		}
	}else{
		if(lockHolders.get(key)){
			if(lockHolders.get(key).workerId !== data.workerId && lockHolders.get(key).type === 'Write')	{
				failReleaseLock(data.lock);
			}else{
				lockHolders.get(key).count += 1;
				releaseLock(data.lock);	

			}	
		}else{
			lockHolders.set(key,data);
			releaseLock(data.lock);

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


function buildProxy(obj) {
    return obj;
}


const java = {lang: {
    Thread: class Thread {
        start() {
            this.run()
        }
        constructor(obj) {
            if (obj) {
                return obj;
            }
        }
    }

}}