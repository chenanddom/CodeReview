package com.itdom.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itdom.elasticsearch.entity.User;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class ElasticSearchBatchInsertTest {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")
                ));
        batchInsert(client);
//        batchDelete(client);

        client.close();
    }

    /**
     * 批量删除
     * @param client
     * @throws IOException
     */
    public static void batchDelete(RestHighLevelClient client) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new DeleteRequest().index("users").id("10001"));
        bulkRequest.add(new DeleteRequest().index("users").id("10002"));
        bulkRequest.add(new DeleteRequest().index("users").id("10003"));

        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        for (BulkItemResponse responseItem : response.getItems()) {
            System.out.println(responseItem.getResponse().getResult());
        }
    }


    /**
     * 批量添加
     * @param client
     * @throws IOException
     */
    public static void batchInsert(RestHighLevelClient client) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();
        user.setUsername("张三");
        user.setAge(25);
        user.setSex("男");
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest().index("users").id("10001").source(objectMapper.writeValueAsString(user),XContentType.JSON));
        User user2 = new User();
        user2.setUsername("李思");
        user2.setAge(23);
        user2.setSex("女的");
        bulkRequest.add(new IndexRequest().index("users").id("10002").source(objectMapper.writeValueAsString(user2),XContentType.JSON));
        User user3 = new User();
        user3.setUsername("王五");
        user3.setAge(28);
        user3.setSex("男的");
        bulkRequest.add(new IndexRequest().index("users").id("10003").source(objectMapper.writeValueAsString(user2),XContentType.JSON));

        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        for (BulkItemResponse responseItem : response.getItems()) {
            System.out.println(responseItem.getResponse().getResult());
        }
    }

}
