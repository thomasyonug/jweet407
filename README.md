## 项目名称
JSweet407 是一个 Java2JS 的转译器实现，在 Jsweet 的基础上，增加了对 Java 并发特性
的支持。JSweet407 主要分为两个部分，转译器扩展与 JS 多线程运行时。其中所支持的特性包
括 Thread 类, Runnable 接口, synchronized 关键字等。此外，还支持部分标准类库中的线程方法
如 join，sleep 等。基于当前的设计架构，还可以方便地实现 Java 标准库中的各种锁类，我们已
在项目中实现了如 ReentrantLock, ReadWriteLock, StampedLock 等类作为示例。

## 文档
根目录下的technical-report.pdf文件中，详细说明了本项目的设计，架构，实现以及不足，并有相关数据展示。

## 运行条件
* JDK16+
* TSC 



## 运行说明
我们提供了一个bash脚本用于编译文件查看结果。run.sh脚本会调用artifacts目录下我们已经编译好的jar包对example文件夹里的文件进行编译，注意example文件夹里一次只能放一个文件否则可能会引起冲突。run.sh会将编译中间结果放到tsout文件目录下，并且将最终编译出来的js代码拷贝到runtime/src/compiled.js中，使用者可以使用http服务器访问runtime/src/test.html查看代码运行结果。具体过程可以查看我们制作的验收视频example.mkv。


## 测试说明
所有测试用例都在项目根目录下的benchmark文件夹内。



## 作者
* 杨文章
* 钟俊杰
* 王连左
* 张世平
* 叶宇航
* 陈奥
