package com.itdom.controller;

import com.itdom.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

        return authService.authUser(clientid, username, password);
    }

    @PostMapping("/superuser")
    public ResponseEntity superUser(@RequestParam("clientid") String clientid,
                                    @RequestParam("username") String username) {
        return authService.superUser(clientid, username);
    }

    @PostMapping("/acl")
    public ResponseEntity acl(@RequestParam("access") int access,
                              @RequestParam("username") String username,
                              @RequestParam("clientid") String clientid,
                              @RequestParam("ipaddr") String ipaddr,
                              @RequestParam("topic") String topic,
                              @RequestParam("mountpoint") String mountpoint) {
        logger.info("EMQX发起客户端操作授权查询请求,access={},username={},clientid={},ipaddr= {},topic={},mountpoint={}",
                access, username, clientid, ipaddr, topic, mountpoint);
        if (username.equals("chendom") && topic.equals("testtopic/#") && access == 1) {
            logger.info("客户端{}有权限订阅{}", username, topic);
            return new ResponseEntity(HttpStatus.OK);
        }
        if (username.equals("emq-client-id-001") && topic.equals("testtopic/123") && access == 2) {
            logger.info("客户端{}有权限向{}发布消息", username, topic);
            return new ResponseEntity(null, HttpStatus.OK);
        }
        logger.info("客户端{},username={},没有权限对主题{}进行{}操作", clientid, username, topic, access == 1 ? "订阅" : "发布");
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);//无权限
    }

    /**
     * 使用webhook监督客户端的状态变化
     *
     * @param body
     */
    @PostMapping("/webhook")
    public void webhook(@RequestBody Map<String, Object> body) {
        authService.webhook(body);
    }

    @GetMapping("/getDeviceStatus")
    public Map getDeviceStatus(@RequestParam("clientId")String clientId){
        return authService.getDeviceStatus(clientId);
    }
}
