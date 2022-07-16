# MyBatis二级缓存源码剖析

org.apache.ibatis.executor.CachingExecutor.query(org.apache.ibatis.mapping.MappedStatement, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler, org.apache.ibatis.cache.CacheKey, org.apache.ibatis.mapping.BoundSql)
源码如下:
```java
 @Override
  public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql)
      throws SQLException {
    Cache cache = ms.getCache();
    if (cache != null) {
       //确定是否配置了flushCache
      flushCacheIfRequired(ms);
      //判断是否开启了useCache并且resultHandler是为空的  
      if (ms.isUseCache() && resultHandler == null) {
        //确保没有输出参数
        ensureNoOutParams(ms, boundSql);
        @SuppressWarnings("unchecked")
        //从二级缓存处获取    
        List<E> list = (List<E>) tcm.getObject(cache, key);
        //如果二级缓存没有就调用代理对象去获取
        if (list == null) {
          list = delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
            //将获取到的数据放到二级缓存
          tcm.putObject(cache, key, list); // issue #578 and #116
        }
        return list;
      }
    }
    return delegate.query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
  }
```


```java
public class CachingExecutor implements Executor {
  //此方法调用的是TransactionalCacheManager的org.apache.ibatis.cache.TransactionalCacheManager.commit()
  @Override
  public void commit(boolean required) throws SQLException {
    delegate.commit(required);
    tcm.commit();
  }
}

```

org.apache.ibatis.cache.TransactionalCacheManager.java
```java
public class TransactionalCacheManager {
  private final Map<Cache, TransactionalCache> transactionalCaches = new HashMap<>();
  public void clear(Cache cache) {
    getTransactionalCache(cache).clear();
  }
  public Object getObject(Cache cache, CacheKey key) {
    return getTransactionalCache(cache).getObject(key);
  }
  public void putObject(Cache cache, CacheKey key, Object value) {
    getTransactionalCache(cache).putObject(key, value);
  }
  public void commit() {
    for (TransactionalCache txCache : transactionalCaches.values()) {
      txCache.commit();
    }
  }
  public void rollback() {
    for (TransactionalCache txCache : transactionalCaches.values()) {
      txCache.rollback();
    }
  }
  private TransactionalCache getTransactionalCache(Cache cache) {
    return MapUtil.computeIfAbsent(transactionalCaches, cache, TransactionalCache::new);
  }
}
```

![二级缓存存在脏读的原因剖析](./files\mybatis-second-cache-1.PNG)