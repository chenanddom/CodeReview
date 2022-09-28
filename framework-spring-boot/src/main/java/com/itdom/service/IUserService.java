package com.itdom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itdom.dao.UserMapper;
import com.itdom.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chendom
 * @since 2022-09-28
 */
@Service
public class IUserService /*extends IService<User> */ {

    @Autowired
    private UserMapper userMapper;

    public void insertA() {
        User user = new User();
        user.setAge(20);
        user.setUsername("A");
        int count = userMapper.insert(user);
    }

//    @Transactional(propagation = Propagation.REQUIRED)
//    @Transactional(propagation = Propagation.MANDATORY)
    public void insertTest() {
        insertA();
        insertBC();
    }

    //    @Transactional(propagation = Propagation.SUPPORTS)
//    @Transactional(propagation = Propagation.MANDATORY)
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Transactional(propagation = Propagation.NOT_SUPPORTED)
//    @Transactional(propagation = Propagation.NEVER)
    @Transactional(propagation = Propagation.NESTED)
    public void insertBC() {
        insertB();
        double i = 10 / 0;
        insertC();
    }

    public void insertB() {
        User user = new User();
        user.setAge(26);
        user.setUsername("B");
        int count = userMapper.insert(user);
    }

    public void insertC() {
        User user = new User();
        user.setAge(28);
        user.setUsername("C");
        int count = userMapper.insert(user);
    }
}
