package com.itdom.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itdom.elasticsearch.entity.User;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ElasticSearchTest {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchTest.class);
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")
                ));

//        createIndex(client);
//        searchIndex(client);
//        deleteIndex(client);
        insertIndex(client);
//        updateUsers(client);
//        searchUser(client);
//        deleteUser(client);
        client.close();
    }

    /**
     * 删除数据
     * @param client
     * @throws IOException
     */
    public static void deleteUser(RestHighLevelClient client) throws IOException {
        DeleteRequest request = new DeleteRequest();
        request.id("10001").index("users");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    /**
     * 查找文档
     * @param client
     * @throws IOException
     */
    public static void searchUser(RestHighLevelClient client) throws IOException {
        GetRequest getRequest = new GetRequest();
        getRequest.index("users").id("10001");
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(response.getSource());
    }


    public static void updateUsers(RestHighLevelClient client) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("users").id("10001");
        updateRequest.doc(XContentType.JSON,"sex","女的","username","李思");
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.getGetResult());

    }


    public static void insertIndex(RestHighLevelClient client) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User();
        user.setUsername("张三");
        user.setAge(28);
        user.setSex("男的");
        IndexRequest request = new IndexRequest();
        request.index("users").id("10001");
        request.source(objectMapper.writeValueAsString(user),XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getResult());
    }


    public static void deleteIndex(RestHighLevelClient client) throws IOException {
        DeleteIndexRequest users = new DeleteIndexRequest("users");
        AcknowledgedResponse acknowledgedResponse = client.indices().delete(users, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.isAcknowledged());
    }


    /**
     * 获取索引
     * @param client
     * @throws IOException
     */
    public static void searchIndex(RestHighLevelClient client) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest("users");
        GetIndexResponse getIndexResponse = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(getIndexResponse.getAliases());
        System.out.println(getIndexResponse.getMappings());
        System.out.println(getIndexResponse.getSettings());


    }




    /**
     * 创建索引
     * @param client
     * @throws IOException
     */
    public static void createIndex(RestHighLevelClient client) throws IOException {
        //创建索引
        CreateIndexResponse response = client.indices().create(new CreateIndexRequest("users"), RequestOptions.DEFAULT);
        boolean acknowledged = response.isAcknowledged();
        logger.debug("创建是否成功:{}",acknowledged);
    }




}
