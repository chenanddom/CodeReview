package com.itdom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nginx")
public class NginxController {
 private static final Logger logger = LoggerFactory.getLogger(NginxController.class);
    @Value("${server.port}")
    private String port;
//    @LocalServerPort
//    private int port2;

    @GetMapping("/get")
    public String getPort(){
        return "ther server.port is:"+port;
    }

}
