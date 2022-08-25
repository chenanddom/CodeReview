package com.itdom.canal;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.xml.internal.bind.v2.TODO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static void main(String args[]) throws InvalidProtocolBufferException {
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress("192.168.1.195", 11111), "example", "", "");
        while (true) {
            //TODO连接
            canalConnector.connect();
            //TODO 订阅
            canalConnector.subscribe("canal_db.*");
            //TODO 获取数据
            Message message = canalConnector.get(1);
            List<Entry> entries = message.getEntries();
            if (entries.size() <= 0) {
                logger.info("没有获取到数据，休整一会。。。");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                for (Entry entry : entries) {
                    //获取表名
                    String tableName = entry.getHeader().getTableName();
                    //获取类型
                    EntryType entryType = entry.getEntryType();
                    //获取序列化后的数据
                    ByteString storeValue = entry.getStoreValue();
                    if (EntryType.ROWDATA.equals(entryType)) {
                        //反序列化
                        RowChange rowChange = RowChange.parseFrom(storeValue);
                        EventType eventType = rowChange.getEventType();
                        List<RowData> rowDatasList = rowChange.getRowDatasList();
                        for (RowData rowData : rowDatasList) {
                            JSONObject before = new JSONObject();
                            List<Column> beforeColumnsList = rowData.getBeforeColumnsList();
                            for (Column column : beforeColumnsList) {
                                before.put(column.getName(), column.getValue());
                            }

                            List<Column> afterColumnsList = rowData.getAfterColumnsList();
                            JSONObject after = new JSONObject();
                            for (Column column : afterColumnsList) {
                                after.put(column.getName(), column.getValue());
                            }

                            logger.info("table:{},EventType:{},Before:{},After:{}", tableName, eventType, before, after);
                        }
                    } else {
                        logger.info("当前操作类型为:{}", entryType);
                    }
                }
            }
        }
    }
}
