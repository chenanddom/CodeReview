package com.itdom.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private static final Map<String, String> map = new ConcurrentHashMap<String, String>(16) {{
        put("chendom", "chendom@123");
        put("admin", "public");
        put("emq-client-id-001", "123456");
    }};


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
}
