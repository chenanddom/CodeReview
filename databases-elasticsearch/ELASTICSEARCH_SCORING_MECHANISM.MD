# Elaticsearch的评分机制
例子：
```json5
GET /shopping/_search?explain=true
{
  "query": {
    "match": {
      "product_name": "华为手机"
    }
  }
}

//响应
{
  "took" : 78,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 4,
      "relation" : "eq"
    },
    "max_score" : 2.2002316,
    "hits" : [
      {
        "_shard" : "[shopping][0]",
        "_node" : "owLuh_R8SU2zLBjv2ZqN3Q",
        "_index" : "shopping",
        "_id" : "orU7WYMBr5sFkuRAutuT",
        "_score" : 2.2002316,
        "_source" : {
          "product_name" : "小米手机",
          "price" : 4999.0
        },
        "_explanation" : {
          "value" : 2.2002316,
          "description" : "sum of:",
          "details" : [
            {
              "value" : 1.1001158,
              "description" : "weight(product_name:手 in 0) [PerFieldSimilarity], result of:",
              "details" : [
                {
                  "value" : 1.1001158,
                  "description" : "score(freq=1.0), computed as boost * idf * tf from:",
                  "details" : [
                    {
                      "value" : 2.2,
                      "description" : "boost",
                      "details" : [ ]
                    },
                    {
                      "value" : 1.2039728,
                      "description" : "idf, computed as log(1 + (N - n + 0.5) / (n + 0.5)) from:",
                      "details" : [
                        {
                          "value" : 1,
                          "description" : "n, number of documents containing term",
                          "details" : [ ]
                        },
                        {
                          "value" : 4,
                          "description" : "N, total number of documents with field",
                          "details" : [ ]
                        }
                      ]
                    },
                    {
                      "value" : 0.41533542,
                      "description" : "tf, computed as freq / (freq + k1 * (1 - b + b * dl / avgdl)) from:",
                      "details" : [
                        {
                          "value" : 1.0,
                          "description" : "freq, occurrences of term within document",
                          "details" : [ ]
                        },
                        {
                          "value" : 1.2,
                          "description" : "k1, term saturation parameter",
                          "details" : [ ]
                        },
                        {
                          "value" : 0.75,
                          "description" : "b, length normalization parameter",
                          "details" : [ ]
                        },
                        {
                          "value" : 4.0,
                          "description" : "dl, length of field",
                          "details" : [ ]
                        },
                        {
                          "value" : 3.25,
                          "description" : "avgdl, average length of field",
                          "details" : [ ]
                        }
                      ]
                    }
                  ]
                }
              ]
            },
...

```
从上面的响应结果可以知道
score=boost * idf * tf
 
1.1001158=2.2 *  1.2039728 * 0.41533542 

* TF(词频)
    Term Frequency:搜索文本中的各个词条(term)在查询文本中出现的多少次，出现的饿次数越多，就越相关，得分就比较高
*　IDF(逆文档频率)
    Inverse Document Frequency:搜索文本中的各个词条(term)在整个索引的所有文档中出现多少，出现的次数越多，说明越不总要，也就是越不
    相关。得分比较低。