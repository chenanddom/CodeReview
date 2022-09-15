package com.itdom.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itdom.elasticsearch.entity.User;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

public class ElasticSearchSearchTest {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")
                ));
//        query(client);
//        queryForCondtion1(client);
//        queryForPage(client);
        combinationQuery(client);
        client.close();
    }

    /**
     * 组合查询
     *
     * @param client
     * @throws IOException
     */
    public static void combinationQuery(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("users");
        //构建查询条件

        SearchSourceBuilder builder = new SearchSourceBuilder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //must就是条件与查询
/*        boolQueryBuilder.must(QueryBuilders.matchQuery("age","25"));
        boolQueryBuilder.must(QueryBuilders.matchQuery("sex","男的"));
//        条件或查询
        boolQueryBuilder.should(QueryBuilders.matchQuery("age","23"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("age","25"));
        builder.query(boolQueryBuilder);*/
        /**
         * 范围查询
         */
/*        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
        rangeQuery.gt(23);
        rangeQuery.lte(25);
        builder.query(rangeQuery);*/

        //模糊查询
/*        FuzzyQueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("username", "张三").fuzziness(Fuzziness.ONE);
        builder.query(queryBuilder);
        request.source(builder);*/
        //高亮查询
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("username", "张三");
//
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.preTags("<font color='red'>").postTags("</font>");
//        highlightBuilder.field("username");
//        builder.highlighter(highlightBuilder);
//        builder.query(matchQueryBuilder);
        /**
         * 聚合查询
         */
//        AggregationBuilders.max("ageMax").field("age")
        //分组
//        AggregationBuilders.terms("ageGroup").field("age");
        builder.aggregation(AggregationBuilders.max("ageMax").field("age"));
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //获取所有的结果
        SearchHits hits = response.getHits();
        System.out.println(response.getTook());
        System.out.println(hits.getTotalHits());
        for (SearchHit hit : hits) {
            System.out.println(new ObjectMapper().writeValueAsString(hit));
        }
    }

    /**
     * 分页查询
     *
     * @param client
     * @throws IOException
     */
    public static void queryForPage(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("users");
        //返回显式的列
        String[] includes = {"username", "age"};
        //不要返回的列
        String[] excludes = {"sex"};
        //构建查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        request.source(searchSourceBuilder.query(QueryBuilders.matchAllQuery()).fetchSource(includes, excludes));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(5);
        //降序，此处因为是文本无法排序
//        searchSourceBuilder.sort("age", SortOrder.DESC);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //获取所有的结果
        SearchHits hits = response.getHits();
        System.out.println(new ObjectMapper().writeValueAsString(response.getHits().getTotalHits()));
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
    }


    public static void queryForCondtion1(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("users");
        //返回显式的列
        String[] includes = {"username", "age"};
        //不要返回的列
        String[] excludes = {"sex"};
        //构建查询条件
        request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("age", "23")).fetchSource(includes, excludes));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //获取所有的结果
        SearchHits hits = response.getHits();
        System.out.println(new ObjectMapper().writeValueAsString(response.getHits().getTotalHits()));
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
    }

    public static void query(RestHighLevelClient client) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("users");
        //返回显式的列
        String[] includes = {"username", "age"};
        //不要返回的列
        String[] excludes = {"sex"};
        //构建查询条件
        request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).fetchSource(includes, excludes));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //获取所有的结果
        SearchHits hits = response.getHits();
        System.out.println(new ObjectMapper().writeValueAsString(response.getHits().getTotalHits()));
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
    }


}
