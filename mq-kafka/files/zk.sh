#!/bin/bash

case $1 in
"start"){
	for i in kafka-node1 kafka-node2 kafka-node3
	do
		echo  ------------- zookeeper $i 启动 ------------
		ssh $i "/opt/module/apache-zookeeper/bin/zkServer.sh start"
	done
}
;;
"stop"){
	for i in kafka-node1 kafka-node2 kafka-node3
	do
		echo  ------------- zookeeper $i 停止 ------------
		ssh $i "/opt/module/apache-zookeeper/bin/zkServer.sh stop"
	done
}
;;
"status"){
	for i in kafka-node1 kafka-node2 kafka-node3
	do
		echo  ------------- zookeeper $i 状态 ------------
		ssh $i "/opt/module/apache-zookeeper/bin/zkServer.sh status"
	done
}
;;
esac

