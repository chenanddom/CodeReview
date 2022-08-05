#!/bin/bash
case $1 in
"start"){
	for i in kafka-node1 kafka-node2 kafka-node3
	do
		echo  ------------- kafka $i 启动 ------------
		ssh $i "/opt/module/kafka/bin/kafka-server-start.sh -daemon /opt/module/kafka/config/server.properties"
	done
}
;;
"stop"){
	for i in kafka-node1 kafka-node2 kafka-node3
	do
		echo  ------------- kafka $i 停止 ------------
		ssh $i "/opt/module/kafka/bin/kafka-server-stop.sh"
	done
}
;;
esac
