package com.itdom.service;

import com.itdom.controller.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final Map<String, String> map = new ConcurrentHashMap<String, String>(16) {{
        put("chendom", "chendom@123");
        put("admin", "public");
        put("emq-client-id-001", "123456");
    }};
    private Map<String, Boolean> clientStatusMap = new HashMap<String, Boolean>();


    public ResponseEntity authUser(String clientid, String username, String password) {
        if (!map.containsKey(username)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        String pswd = map.get(username);
        if (!password.equals(pswd)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity superUser(String clientid, String username) {
        logger.info("emqx 查询是否是超级用户,clientid={},username={}", clientid, username);
        if (clientid.contains("admin") || username.contains("admin")) {
            logger.info("用户{}是超级用户", username);
            //是超级用户
            return new ResponseEntity<Object>(HttpStatus.OK);
        } else {
            logger.info("用户{}不是超级用户", username);
            //不是超级用户
            return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
        }
    }

    public void webhook(Map<String, Object> body) {
        String action = (String) body.get("action");
        String clientId = (String) body.get("clientid");
        if (action.equals("client_connected")) {
            //客户端成功接入
            clientStatusMap.put(clientId, true);
        }
        if (action.equals("client_disconnected")) {
            //客户端断开连接
            clientStatusMap.put(clientId, false);
        }
    }

    public Map getDeviceStatus(String clientId) {
        if (StringUtils.isEmpty(clientId)){
            return clientStatusMap;
        }
        final String cid = clientId;
        return new HashMap<String,Boolean>(){{put(cid,clientStatusMap.get(cid));}};
    }
}
