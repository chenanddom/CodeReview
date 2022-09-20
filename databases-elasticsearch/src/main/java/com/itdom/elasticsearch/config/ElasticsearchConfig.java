package com.itdom.elasticsearch.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "elasticsearch")
@Configuration
@Data
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private List<String> nodes;

    //重写父类方法
    @Override
    public RestHighLevelClient elasticsearchClient() {
        List<Node> nodeList = new ArrayList<>();
        for (String node : nodes) {
            String[] nodeIpPort = node.split(":");
            nodeList.add(new Node(new HttpHost(nodeIpPort[0], Integer.valueOf(nodeIpPort[1]))));
        }
        RestClientBuilder builder = RestClient.builder(nodeList.get(0),nodeList.get(1),nodeList.get(2));
        RestHighLevelClient restHighLevelClient = new
                RestHighLevelClient(builder);
        return restHighLevelClient;
    }
}

