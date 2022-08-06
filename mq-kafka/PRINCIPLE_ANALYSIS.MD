#Kafka原理剖析
## 生产者的发送原理
![Producer发送原理](./files/kafka-principle-1.PNG)
```text
在消息发送的过程中，涉及到了两个线程——main 线程和Sender 线程。在main 线程
中创建了一个双端队列RecordAccumulator。main 线程将消息发送给RecordAccumulator，
Sender 线程不断从RecordAccumulator 中拉取消息发送到Kafka Broker。

Sender线程最大可以缓冲5个请求，如果broker在这五个请求过去之后还没有任何响应，
就会拒收第五个请求之后的所有请求。

如果broker接收消息成功了，那么就会反馈状态给Sender，如果成功，就会将RecordAccumulate线程的DQueue
RecordAccumulate的大小是32M，每个producer Batch有16k，没满16k就作为一批发送给broker，如果一直没有满，在
达到了linger time之后就也会发送，默认情况linger time是0，就是一有消息就接收，但是这样效率不高。



```


