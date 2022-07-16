package com.itdom.customercache;

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

public class RedisCacheTest {
    private Logger logger = LoggerFactory.getLogger(RedisCacheTest.class);
    private  InputStream stream;
    @Before
    public void init() {
        String path = "mybatis.xml";
        try {
            stream = Resources.getResourceAsStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testRedisCache() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        sqlSession.selectOne("com.itdom.mapper.ManagementMapper.getById",1);
        sqlSession.commit();
        InputStream inputStream2 = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory sqlSessionFactory2 = new SqlSessionFactoryBuilder().build(inputStream2);
        SqlSession sqlSession2 = sqlSessionFactory2.openSession();
        sqlSession2.selectOne("com.itdom.mapper.ManagementMapper.getById",1);
        sqlSession2.commit();
    }
}
