package com.itdom;

import com.itdom.entity.Management;
import com.itdom.mapper.ManagementMapper;
import org.apache.ibatis.io.Resources;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MybatisApplicationTest {
    private static final Logger logger = LoggerFactory.getLogger(MybatisApplicationTest.class);
    private SqlSession sqlSession;
    @Before
    public void init(){
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
    public void sampleParam(){
        ManagementMapper mapper = sqlSession.getMapper(ManagementMapper.class);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id",1);
        map.put("test",2);
        List<Management> managementList = mapper.getById4(map);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("id",1);
        map2.put("test",2);
        List<Management> managementList2 = mapper.getById4(map2);

    }

}
