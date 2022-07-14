package com.itdom;

import com.itdom.entity.Management;
import com.itdom.mapper.ManagementMapper;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MybatisApplicationTest {
    private static final Logger logger = LoggerFactory.getLogger(MybatisApplicationTest.class);
    private SqlSession sqlSession;

    @Before
    public void init() {
        String path = "mybatis.xml";
        InputStream stream = null;
        try {
            stream = Resources.getResourceAsStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(stream);
        sqlSession = sqlSessionFactory.openSession();
    }

    //@Resource
//private ManagementMapper managementMapper;
    @Test
    public void findManagement() throws IOException {
//
//    List<Management> managements = managementMapper.getById(1);
//    logger.debug("1:"+managements);
//    List<Management> managements2 = managementMapper.getById(1);
//    logger.debug("2:"+managements);
//        String path = "mybatis.xml";
//        InputStream stream = Resources.getResourceAsStream(path);
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(stream);
//        SqlSession sqlSession = sqlSessionFactory.openSession();
        ManagementMapper mapper = sqlSession.getMapper(ManagementMapper.class);
        List<Management> managementList = mapper.getById(1);
        List<Management> managementList2 = mapper.getById2(1);
    }

    @Test
    public void sampleParam() {
        ManagementMapper mapper = sqlSession.getMapper(ManagementMapper.class);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 1);
        map.put("test", 2);
        List<Management> managementList = mapper.getById4(map);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("id", 1);
        map2.put("test", 2);
        List<Management> managementList2 = mapper.getById4(map2);

    }

    /**
     * 分页参数必须相同，否则无法命中缓存
     */
    @Test
    public void pageSample() {
//        ManagementMapper mapper = sqlSession.getMapper(ManagementMapper.class);
        RowBounds rowBounds = new RowBounds(0, 1);
        sqlSession.selectList("com.itdom.mapper.ManagementMapper.list", null, rowBounds);
        RowBounds rowBounds2 = new RowBounds(0, 2);
        sqlSession.selectList("com.itdom.mapper.ManagementMapper.list", null, rowBounds2);
    }

    /**
     * 如果调用了sqlSession的Update的相关方法，那么Mybatis的一级缓存就会失效
     */
    @Test
    public void updateDBInvalidCache() {
        //虽然实际上是查询，但是调用了sqlSession的Update方法就缓存就会失效
        sqlSession.update("com.itdom.mapper.ManagementMapper.getById", 1);
        //缓存失效就会转到数据库进行查询
        sqlSession.update("com.itdom.mapper.ManagementMapper.getById", 1);

    }

    /**
     * 关闭sqlSession失效缓存
     */
    @Test
    public void closeSqlSessionInvalidCache() throws NoSuchFieldException, IllegalAccessException {
        sqlSession.selectOne("com.itdom.mapper.ManagementMapper.getById", 1);
//        sqlSession.close();
//        sqlSession.selectOne("com.itdom.mapper.ManagementMapper.getById", 1);
        Field executorField = sqlSession.getClass().getDeclaredField("executor");
        executorField.setAccessible(true);
        CachingExecutor cachingExecutor = (CachingExecutor) executorField.get(sqlSession);
        Field delegate = cachingExecutor.getClass().getDeclaredField("delegate");
        delegate.setAccessible(true);
        SimpleExecutor simpleExecutor = (SimpleExecutor) delegate.get(cachingExecutor);

        Field declaredField = simpleExecutor.getClass().getSuperclass().getDeclaredField("localCache");
        declaredField.setAccessible(true);
        PerpetualCache perpetualCache = (PerpetualCache) declaredField.get(simpleExecutor);

        Field field = perpetualCache.getClass().getDeclaredField("cache");
        field.setAccessible(true);
        Map<Object, Object> cache = (Map<Object, Object>) field.get(perpetualCache);
        Set<Map.Entry<Object, Object>> entrySet = cache.entrySet();
        System.out.println("-----------------------------------------");
        for (Map.Entry<Object, Object> objectObjectEntry : entrySet) {
            System.out.println(objectObjectEntry.getKey()+"--"+objectObjectEntry.getValue());
        }
        sqlSession.close();
        System.out.println("-----------------------------------------");
        for (Map.Entry<Object, Object> objectObjectEntry : entrySet) {
            System.out.println(objectObjectEntry.getKey()+"--"+objectObjectEntry.getValue());
        }
    }

    /**
     * 回滚事务会缓存失效
     */
    @Test
    public void commitTransactionClearCache(){
            sqlSession.selectOne("com.itdom.mapper.ManagementMapper.getById",1);
            sqlSession.commit();
            sqlSession.selectOne("com.itdom.mapper.ManagementMapper.getById",1);
    }
    /**
     * 回滚事务会缓存失效
     */
    @Test
    public void rollbackTransactionClearCache(){
        sqlSession.selectOne("com.itdom.mapper.ManagementMapper.getById",1);
        sqlSession.rollback();
        sqlSession.selectOne("com.itdom.mapper.ManagementMapper.getById",1);
    }
}
