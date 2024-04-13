# TODO2

## 1. 递归get set

```
class Foo {
    a: {
        b: 1
    }
}

var foo = new Foo()
foo.a = {b: 2}

foo.a = 1;
```
这种代码会让foo.a.b的proxy失效。
我们需要去参考Vue框架的做法，Vue.set。
## 2. ChannelCenter
每次set的时候，都需要将value作为消息传给其他worker。
如果是一个Object，那么需要在object中记录下当前所属的通道。
```
var Desk = new Proxy(__Desk, {
    set(obj, prop, value) {
        if (value instanceof Object) {
            ChannelCenter.record_channel(obj, prop, value);   
        }
        obj[prop] = value;
        ChannelCenter.update(obj, prop, value);
        return true;
    }
})

function record_channel(obj, prop, value) {
    value.__channel = `${obj}.${prop}`
}

function update(obj, prop, value) {
    get_channels(obj, prop).forEach(channel => channel.postMsg(obj, prop, value))
}


```
sync对应的是synchronized关键词，接受一个object，
创建一个sharedBuffer挂在这个object上，然后通过之前记录的通道传出去。
此外需要在这个object上挂上wait和notify这类锁方法。
``` 
ChannelCenter.sync((Desk.lock_$LI$()));

function sync(obj) {
    ChannelCenter.exist_monitor(obj.__channel);
    if (obj.exist_monitor()) {
        obj.get_monitor().
    }
}
```
这两个方法会调用相应的atomic上的方法。
``` 
Desk.lock_$LI$().wait();
Desk.lock_$LI$().notifyAll();
```


