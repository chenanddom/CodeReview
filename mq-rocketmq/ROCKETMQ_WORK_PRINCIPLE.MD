# RocketMQ的工作原理
## 消息的生产
Producer可以将消息写入到某个Broker中的Queue中，其经历如下的过程：
* 消息的生产过程
    * Producer发送消息之前，会先想NameServer发出获取消息Topic的路由信息的请求
    * NameServer返回该Topic的`路由表`及Broker的列表
    * Producer根据代码中指定的Queue选择策略，从Queue列表中选出一个队列，用于后续的存储消息
    * Producer对消息做一些特殊的处理，例如，消息本身超过4M，则会对其进行压缩。
    * Producer向出的Queue所在的Broker法出RPC请求，将消息发送到选择的Queue。
> 路由表：实际是一个Map，key为Topic名称，value是一个QueueData实例列表。QueueData并不是一个Queue对应一个QueueData，
> 而是一个Broker中该Topic的所有Queue对应一个QueueData。即，只要涉及到该Topic的Broker，一个Broker对应一个QueueData。
> QueueData中包含brokerName。简单来说，路由表的key为Topic名称，value则为所有涉及该Topic的BrokerName列表。    


> Broker列表：其实际也是一个Map。key为brokerName，value为BrokerData。一个Broker对应一个BrokerData实例，对吗？不对。
> 一套BrokerName名称相同的Master-Slave小集群对应一个BrokerData。BrokerData中包含brokername以及一个Map.该Map的key为
> brokerId,value为该broker的对应的地址。brokerId为0标识broker为Master,非0标识Slave。



## Queue的选择算法
对于无序消息，其Queue选择算法，也称为消息投递算法，常见的有两种：

* 轮询算法
默认选择算法。该算法保证了每个Queue中可以均匀的获取到消息。
> 该算法存在一个问题:由于某些原因，在某些Broker上的Queue可能投递延迟比较严重。从而导致Producer的缓存队列中出现较大的消息积压，
>影响到了消息的投递性能。

* 最小投递延迟算法
该算法会统计消息投递的时间延迟，然后根据统计出结果将消息投递到时间延迟最新小的Queue。如果延迟相同，则采用轮询算法投递。
该算法可以有效提升消息的投递性能 
> 该算法存在一个问题：消息在Queue上的分配不均匀。投递延迟小的Queue其可能会存在大量的消息。而对该Queue的消费者压力会增大，
>降低消息的消费能力，可能会导致MQ中的消息堆积

  

## 消息的存储
RocketMQ中的消息存储在本地文件系统中，这些相关文件默认在当前用户主目录下的store目录中。

* store目录里文件的含义
    * abort:该文件在Broker启动的之后会自动创建，正常关闭Broker，该文件会自动小时。若在没有启动的Broker的情况下，
    发现这个文件是存在的，说明之前Broker的关闭是非正常关闭的
    * checkpoint:其中存储者commitlog,cosumerqueue,index文件的最后刷盘时间戳。
    * commitlog:其中存储在commitlog文件，而消息就是写在commitlog文件中的
    * config:存放者Broker允许器间的一些配置数据
    * consumerqueue：其中存放者consumerqueue文件，队列就存放在这个文件中
    * index:其中存放者消息索引文件indexFile
    * lock:运行器间使用到的全局资源锁。
    


















































































































