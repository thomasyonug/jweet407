/**
 * initWorker
 * 一个简单的worker，是每个worker启动的初始状态
 */
/**
 * 一系列全局变量，在worker的整个生命周期中都可以被调用
 */
// ============== Start of 全局变量 ==============
// 用来查找key对应的port（也对应一个worker）
let keyToPort = new Map();
// cvs更新后的值会在这里
let container = {};
let workerId;
let workerName;

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
			if (container[className] != undefined) {
				return getValue(container, key);
			}
			return _target[propKey];
		},
		set: function (_, propKey, newValue) {
			let key = className + '.' + propKey;
			if (!keyToPort.has(key)) { // 如果设置一个新prop，或者根本就没有其他worker共享
				return true;
			}
			update(key, newValue);
			for (let port of keyToPort.get(key)) {
				port.postMessage({ 'command': 'update', 'key': key, 'value': newValue });
			}
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
    // connect a channel from another worker
    // cvs 是与该worker共享的相同的变量的集合
    // 此事件会发生多次，在worker存在的时候
    case 'connect':
      let workerPort = event.ports[0];
      let cvs = data.cvs;
      // 将每个cv名字（key）都加入到map中
      cvs.forEach(cv => {
        if (!keyToPort.has(cv)) {
          keyToPort.set(cv, new Set());
        }
        keyToPort.get(cv).add(workerPort);
      });
      workerPort.onmessage = (ev) => {
        let data = ev.data;
        let command = data.command;
        if (command === 'update') {
          update(data.key, data.value);
          Logger.info(`${workerName} receive a update of ` + data.key + ':' + data.value);
        }
      };
      break;
    default:
      Logger.warn('Received unknown command:' + event.data.command);
  }
};
/**
 * 辅助函数
 */
// 处理链式更新
function update(path, value) {
  let keys = path.split('.');
  let current = container;
  for (let i = 0; i < keys.length; i++) {
    if (i == keys.length - 1) {
      // if i is the last element, update the value
      current[keys[i]] = value;
    }
    else {
      // if the key not exist, create a new object
      if (!current[keys[i]] || typeof current[keys[i]] != 'object') {
        current[keys[i]] = {};
      }
      current = current[keys[i]];
    }
  }
}
function getValue(obj, key) {
  const keys = key.split('.');
  let current = obj;

  for (let i = 0; i < keys.length; i++) {
    // Check if the property exists in the current object
    if (current[keys[i]] !== undefined) {
      // Move deeper into the object structure
      current = current[keys[i]];
    } else {
      // Return undefined if any key in the chain doesn't exist
      return undefined;
    }
  }

  return current;
}

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