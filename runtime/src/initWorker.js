/**
 * initWorker
 * 一个简单的worker，是每个worker启动的初始状态
 */
// cvs更新后的值会在这里
let workerId;
let workerName;
const BUF_SIZE = 16;
//Atomics.wait的时间单位为毫秒，定义其他时间单位到毫秒的倍数
class TimeUnit{
	static DAYS = 24*60*60*1000;
	static HOURS = 60*60*1000;
	static MINUTES = 60*1000;
	static SECONDS = 1000;
	static MILLISECONDS = 1;
	static MICROSECONDS = 1e-3;
	static NANOSECONDS = 1e-6;
}
function createLock() {
	const lock = new Int32Array(new SharedArrayBuffer(4));
	return lock;
}
//map存储所有改变了的key value
let changedObjects = new Map();
//map存储所有的主线程中的key value
let mainObject = new Map();
class lock{
	__key;
	lock(){	
		Comm.synchronizePostMessage({ 'command': 'sync', 'key': this.__key, "workerId": workerId });
		Comm.query();	
	}
	unlock(){	
		Comm.update(changedObjects);		
		postMessage({ 'command': 'unsync', 'key': this.__key, "workerId": workerId });
	}
	tryLock(time,timeUnit){
		//TODO
		if(arguments.length == 0){
			return Comm.synchronizePostMessageWithReturn({ 'command': 'tryLock', 'key': this.__key, "workerId": workerId })
		}
		else{
			let waitTime= time * timeUnit;
			//console.log(waitTime);
			const lock = createLock();
			postMessage({ 'command': 'sync', 'key': this.__key,'lock':lock, "workerId": workerId });
			if(Atomics.wait(lock, 0, 0,waitTime)==="timed-out"){
				Atomics.store(lock,0,2);
				return false;
			}	
			return true;
		}
	}
	newCondition(){
		let condition = new Condition()
		condition.__lockName = this.__key;
		return condition;
	}
}
class ReentrantLock extends lock{
	
} 
class readLock extends lock{
	lock(){
		
		Comm.synchronizePostMessage({ 'command': 'readLock.lock', 'key': this.__lockName, "workerId": workerId });	
		Comm.query();
	}
	unlock(){
		Comm.update(changedObjects);
		postMessage({ 'command': 'readLock.unlock', 'key': this.__lockName, "workerId": workerId });
	}
	newCondition(){
		let condition = new ReadWriteCondition()
		condition.__lockName = this.__lockName;
		return condition;
	}
}
class writeLock {

	lock(){
		Comm.synchronizePostMessage({ 'command': 'writeLock.lock', 'key': this.__lockName, "workerId": workerId });
		Comm.query();
	}
	unlock(){	
		Comm.update(changedObjects);
		postMessage({ 'command': 'writeLock.unlock', 'key': this.__lockName, "workerId": workerId });
	}
	newCondition(){
		let condition = new Condition()
		condition.__lockName = this.__lockName;
		return condition;
	}
}
class ReentrantReadWriteLock{
	__key;
	rLock = new readLock();
	wLock = new writeLock();
	readLock(){
		this.rLock.__lockName = this.__key;
		this.rLock.__type = "Read";
		return this.rLock;
	}
	writeLock(){
		this.wLock.__lockName = this.__key;
		this.rLock.__type = "Write";
		return this.wLock;
	}
}
class Condition{
	await(){
		Comm.synchronizePostMessage({ 'command': 'await', 'key': this.__key,'lockName':this.__lockName, "workerId": workerId });
	}
	signal(){
		postMessage({ 'command': 'signal', 'key': this.__key,'lockName':this.__lockName, "workerId": workerId });
	}
	signalAll(){
		postMessage({ 'command': 'signalAll', 'key': this.__key,'lockName':this.__lockName, "workerId": workerId });
	}
}
class Comm {
	static sync(obj) {
		const key = obj.__key;
		this.synchronizePostMessage({ 'command': 'sync', 'key': key, "workerId": workerId });
		Comm.query();
	}
	static unsync(obj) {
		Comm.update(changedObjects);
		const key = obj.__key;
		postMessage({ 'command': 'unsync', 'key': key, "workerId": workerId });
	}
	static wait(obj) {
		Comm.update(changedObjects);
		const key = obj.__key;
		this.synchronizePostMessage({ 'command': 'wait', 'key': key, "workerId": workerId });
	}
	static notify(obj) {
		const key = obj.__key;
		postMessage({ 'command': 'notify', 'key': key, "workerId": workerId });
	}
	static update(changedObj) {
		if (changedObj.size === 0) return;
		Logger.info('子线程一次性set:');
		Logger.info(changedObj);
		this.synchronizePostMessage({ 'command': 'update', 'obj': changedObj });
		changedObj.clear();
	}
	static query() {
		const buffer = new SharedArrayBuffer(BUF_SIZE * 2);
		const arr = new Int32Array(buffer);
		let _objects = this.synchronizePostMessageWithData(arr);
		mainObject = new Map([...mainObject, ..._objects]);
	}
	static synchronizePostMessageWithData(arr) { // Int32Array
		postMessage({ 'command': 'query', 'arr': arr });
		Atomics.wait(arr, 0, 0);
		let str = '';
		str += deserialize2MapStr(arr);
		// 如果还有后续的数据传送，要继续接收
		let flag = Atomics.load(arr, 0);
		while (flag !== -1) {
			Logger.debug(str);
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
	static synchronizePostMessageWithReturn(message){
		const lock = createLock();
		postMessage({ ...message, lock });
		Atomics.wait(lock, 0, 0);
		if(Atomics.load(lock,0)===2){
			console.log(3);
			return false;
		}
		return true;
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
			if (mainObject.has(key)) {
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
			if (!(newValue instanceof Object)) { changedObjects.set(key, newValue); }
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

Object.prototype.wait = function () {
    Comm.wait(this)
}

Object.prototype.notify = function () {
    Comm.notify(this)
}

Object.prototype.notifyAll = function () {
    Comm.notify(this)
}
