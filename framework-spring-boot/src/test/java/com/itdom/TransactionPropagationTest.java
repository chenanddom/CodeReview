package com.itdom;

import com.itdom.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionPropagationTest {
    @Autowired
    private IUserService userService;

    @Test
    @Transactional
    public void propagationRequiredTest() {
        userService.insertA();
        userService.insertBC();

    }






}
