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

every class extend WebWorker should have a source field like this:
```js
class Cook extends WebWorker {
    public source = ```
    some js code
    ```
}
```
and this source is used to create a worker
The WebWorker class should also have a start() method, in which we can start a worker using the source