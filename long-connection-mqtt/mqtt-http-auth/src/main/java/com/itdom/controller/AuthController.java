package com.itdom.controller;

import com.itdom.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mqtt")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity authUser(@RequestParam("clientid") String clientid,
                                   @RequestParam("username") String username,
                                   @RequestParam("password") String password) {
        logger.debug("开始认证clientid:{},username:{},password:{}", clientid, username, password);

        return authService.authUser(clientid,username,password);
    }

}
