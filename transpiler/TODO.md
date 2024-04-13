# TODO

## 1. class WebWorker
设计WebWorker类，使用如下方法，将类里的source作为webworker代码
```js
const code = "console.log('Hello from web worker!')"
const blob = new Blob([code], {type: 'application/javascript'})
const worker = new Worker(URL.createObjectURL(blob))
```
WebWorker类型如下
```js
class Webworker {
    public start();
}
```
在start方法中创建worker进程

## 2. 进程通信
Cook和Customer两个类都会用到Desk，需要在start的时候，为两个类初始化消息通道。
### 2.1 通道建立
主线程中利用__captured_cvs，向通信中心注册需要的通道。
例如Cook的cvs集合是
```
{'Thread':Thread,'super':super,'Override':Override,'Desk':Desk,'InterruptedException':InterruptedException,'RuntimeException':RuntimeException,'System':System,}
```
那么start方法里初始化完worker之后，拿到这个worker的handler，然后进行注册
```
start () {
    const worker = new Worker();
    ...
    
    Channel.register(worker, 'Desk.food_flag')
}
```
以上channel可以封装js的MessageChannel来实现，好处是消息传递可以在worker之间传递，不需要经过主线程
https://stackoverflow.com/questions/14191394/web-workers-communication-using-messagechannel-html5
### 2.2 数据传输同步
利用js里的proxy特性，get的时候阻塞等待，set的时候通知所有通道

## 3. complete cvs analyzer
* 准确度还差一点，比如在captured_cvs里会出现super，Thread等等关键字或者标准类库。
* 颗粒度不够，目前只分析到类这一层，更精细的还需要知道类中哪一个成员变量会被用到。

## 4. class Thread
这个类在worker里使用，也只需要提供start方法，主要作用是调用run方法。

## 5. lock的设计
之前提到过，锁住之后如何唤醒一个worker。





