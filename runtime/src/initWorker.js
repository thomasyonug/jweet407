

// 一个简单的worker，当接受到{ command: 'start', source }消息时，会执行source中的代码
//一个cvs对应一个port集合
let workerPorts = {};
//cvs对应值
let objectMap = new Map();
let onPort = (ev) => {
    let data = ev.data;
    let command = data.command;
    if (command === 'update') {
        objectMap.set(data.key, data.value);
        console.log(`receive a update of `+data.key+':'+data.value);
    }
}
let proxyHandler = {
    get: function(target, propKey, receiver) {
        //如果在cvs中,并且已经set就返回objectMap的值
        let key= target.className+'.'+propKey;
        if (objectMap.has(key) ){
            console.log('get value:'+key+':'+objectMap.get(key));
            return objectMap.get(key);
        }
        return target[propKey];
    },
    set: function(target, propKey, newValue, receiver) {
        //console.log('set value');
        target[propKey] = newValue;
        //比如food_flag变成desk.food_flag,因为cvs的是desk.food_flag
        let key= target.className+'.'+propKey;
        //console.log(key);
        console.log(workerPorts[key]);
        //如果在cvs中，就发送消息
        if(workerPorts[key]!==undefined){
            //更新objectMap的值
            objectMap.set(key, newValue);
            //发送消息给所有的workerPort
            workerPorts[key].forEach((workerPort) => {
                workerPort.postMessage({ command: 'update', key: key, value: newValue });
            });
            console.log('set value'+key+':'+newValue);

        }

        return true;
    }
}
self.onmessage = (event) => {
    let data = event.data
    let key = data.key;
    let command = data.command;
    switch (command) {
        case 'start':
            const func = new Function(data.source);
            func();
            break;
        case 'connect':
            let workerPort = event.ports[0];
            if(!workerPorts[key]) {
                workerPorts[key] = [];
                //console.log('create a new workerPort'+key);
            }
            //端口加入到workerPorts[key]中
            workerPorts[key].push(workerPort);
             console.log(key+' connect');
             //console.log(workerPorts[key]);
            workerPort.onmessage = onPort;
            break;
        default:
            console.log('Received unknown command:', event.data.command);
    }
};
