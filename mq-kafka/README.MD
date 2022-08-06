# kafka
传统的定义:kafka是基于分布式的发布订阅的消息队列.

现代:Apache Kafka 是一个开源分布式事件流平台，被数千家公司用于高性能数据管道、流分析、数据集成和任务关键型应用程序.

## 消息队列的两种模式
* 点对点:
    * 消费者主动拉去数据，消息收到后清除消息
![点对点](./files/p2pconsumemsg.PNG)

* 发布/订阅模式:
    * 可以有多个topic主题(比如浏览、订阅、收藏、评论等)
    * 消费者消费数据之后，不删除数据
    * 每个消费者相互独立，都可以消费到数据
    
![发布订阅模式](./files/publish-subscribe.PNG)    
    
## Kafka基础架构
1. 为了方便扩展，并提高吞吐量，一个topic分为多个partition
2. 配合分区的涉及，提高小组的概念，组内每个消费者并行消费
3. 为了提高可用性，未每个partition增加若干副本，类似NameNode HA
4. ZK中记录谁是leader，Kafka2.8.0以后可以配置不采用ZK


## KAFKA安装
[KAFKA安装笔记](./KAFKA-INSTALL.MD)


## Kafka的相关操作

|参数|描述|
| ---- | ---- |
|--bootstrap-server<String:server toconnect to>|连接的Kafka Broker主机名称和端口号|
|--topic<String:topic>|操作的topic名称|
|--create|创建主题|
|--delete|删除主题|
|--alter|修改主题|
|--list|查看所有主题|
|--describe|查看主题详细描述|
|--partitions<Integer:# of partitions>|设置分区数|
|--replication-factor<Integer:replication factor>|设置分区副本|
|--config<String:name=value>|更新系统默认的配置|

```shell script
# 创建分区
[root@kafka-node1 kafka]# ./bin/kafka-topics.sh --bootstrap-server kafka-node1:9092,kafka-node2:9092,kafka-node3:9092 --topic first --create --partitions 1 --replication-factor 3
Created topic first.
#罗列分区
[root@kafka-node1 kafka]# ./bin/kafka-topics.sh --bootstrap-server kafka-node1:9092,kafka-node2:9092,kafka-node3:9092 --list
first
# 查看分区 PartitionCount 分区数 ReplicationFactor 分区副本的数量 Configs: segment.bytes分区一个块的大小，默认是1GB  Topic:first分区的名称  Partition:表示分区从0开始  Leader:1 说明1是leader机器          Replicas: 1,3,2(副本所在的服务实例)
[root@kafka-node1 kafka]# ./bin/kafka-topics.sh --bootstrap-server kafka-node1:9092,kafka-node2:9092,kafka-node3:9092 --topic first --describe
Topic: first	TopicId: ny8JJE15Ra2KhFO3dSDVeQ	PartitionCount: 1	ReplicationFactor: 3	Configs: segment.bytes=1073741824
	Topic: first	Partition: 0	Leader: 1	Replicas: 1,3,2	Isr: 1,3,2
# 修改副本
[root@kafka-node1 kafka]# ./bin/kafka-topics.sh --bootstrap-server kafka-node1:9092,kafka-node2:9092,kafka-node3:9092 --topic first --alter --partitions 2
[root@kafka-node1 kafka]# ./bin/kafka-topics.sh --bootstrap-server kafka-node1:9092,kafka-node2:9092,kafka-node3:9092 --list
first
# 查看topic
[root@kafka-node1 kafka]# ./bin/kafka-topics.sh --bootstrap-server kafka-node1:9092,kafka-node2:9092,kafka-node3:9092 --topic first --describe
Topic: first	TopicId: ny8JJE15Ra2KhFO3dSDVeQ	PartitionCount: 2	ReplicationFactor: 3	Configs: segment.bytes=1073741824
	Topic: first	Partition: 0	Leader: 1	Replicas: 1,3,2	Isr: 1,3,2
	Topic: first	Partition: 1	Leader: 2	Replicas: 2,3,1	Isr: 2,3,1
# 将分区设置小是不允许的
[root@kafka-node1 kafka]# ./bin/kafka-topics.sh --bootstrap-server kafka-node1:9092,kafka-node2:9092,kafka-node3:9092 --topic first --alter --partitions 1
Error while executing topic command : Topic currently has 2 partitions, which is higher than the requested 1.
[2022-08-06 10:04:15,090] ERROR org.apache.kafka.common.errors.InvalidPartitionsException: Topic currently has 2 partitions, which is higher than the requested 1.
 (kafka.admin.TopicCommand$)
```

注意：
1. 分区只能增加不能减少,如果执意操作会报错

```shell script
[root@kafka-node1 kafka]# ./bin/kafka-topics.sh --bootstrap-server kafka-node1:9092,kafka-node2:9092,kafka-node3:9092 --topic first --alter --partitions 1
Error while executing topic command : Topic currently has 2 partitions, which is higher than the requested 1.
[2022-08-06 10:04:15,090] ERROR org.apache.kafka.common.errors.InvalidPartitionsException: Topic currently has 2 partitions, which is higher than the requested 1.
 (kafka.admin.TopicCommand$)
```
2. 副本数不能修改直接使用 --replication-factor 修改


## KAFKA原理剖析
[KAFKA原理剖析](./PRINCIPLE_ANALYSIS.MD)




























    



