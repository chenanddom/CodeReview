package com.itdom.secondcache;

import com.itdom.MybatisApplicationTest;
import com.itdom.entity.Management;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 二级缓存
 */
public class SecondCacheTest {
    private static final Logger logger = LoggerFactory.getLogger(SecondCacheTest.class);
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

    /**
     * 关闭sqlsession仍然可以使用二级缓存
     */
    @Test
    public void testSecondaryCache() {
        InputStream stream = null;
        try {
            stream = Resources.getResourceAsStream("mybatis.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(stream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Management management = sqlSession.selectOne("com.itdom.mapper.ManagementMapper.getById", 1);
        logger.info("management:{}", management);
        sqlSession.close();//关闭sqlsession

        sqlSession = sqlSessionFactory.openSession();
        Management management2 = sqlSession.selectOne("com.itdom.mapper.ManagementMapper.getById", 1);
//        sqlSession.close();
        logger.info("management2:{}", management2);
    }
    @Test
    public void labelSetCache() {
        InputStream stream = null;
        try {
            stream = Resources.getResourceAsStream("mybatis.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(stream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        Management management = sqlSession.selectOne("com.itdom.mapper.ManagementMapper.selectNoneClearCache", 1);
        logger.info("management1:{}", management);
        sqlSession.commit();
        sqlSession.update("com.itdom.mapper.ManagementMapper.updateById", 1);
        sqlSession.commit();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        Management management2 = sqlSession2.selectOne("com.itdom.mapper.ManagementMapper.selectNoneClearCache", 1);
        logger.info("management2:{}", management2);

    }
    @Test
    public void evictionPolicy(){

        InputStream stream = null;
        try {
            stream = Resources.getResourceAsStream("mybatis.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(stream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        Management management = sqlSession.selectOne("com.itdom.mapper.ManagementMapper.findById1", 1);
        sqlSession.commit();
        logger.info("management1:{}",management);
        SqlSession sqlSession2 = sqlSessionFactory.openSession();

        Management management2 = sqlSession2.selectOne("com.itdom.mapper.ManagementMapper.findById2", 1);
        sqlSession2.commit();
        logger.info("management2:{}",management2);

        SqlSession sqlSession3 = sqlSessionFactory.openSession();

        Management management3 = sqlSession3.selectOne("com.itdom.mapper.ManagementMapper.getById", 2);
            sqlSession3.commit();
        logger.info("management3:{}",management3);

        SqlSession sqlSession4 = sqlSessionFactory.openSession();

        Management management4 = sqlSession4.selectOne("com.itdom.mapper.ManagementMapper.findById1", 1);
        sqlSession4.commit();
        logger.info("management4:{}",management4);

    }


}
