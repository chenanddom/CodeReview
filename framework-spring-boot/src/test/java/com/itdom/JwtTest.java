package com.itdom;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
//@RunWith(SpringRunner.class)
public class JwtTest {

    @Test
    public void genericToken(){
        JwtBuilder builder = Jwts.builder();
        //设置jti
        builder.setId("24783");
        builder.setIssuedAt(new Date(System.currentTimeMillis()));
        builder.setAudience("zhangsan");
        //主体，用户{"sub":"zhangsan"}
        builder.setSubject("zhangsan");
        long now = System.currentTimeMillis();
        long exp = now+60*1000;
        builder.setExpiration(new Date(exp));
        builder.signWith(SignatureAlgorithm.HS256, "mustbehaveenoughsizeforthekey39028234567891");

        System.out.println(builder.compact());
        System.out.println("".length());
    }
    @Test
    public void parseToken(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyNDc4MyIsImlhdCI6MTY1MTg0MDM3MiwiYXVkIjoiemhhbmdzYW4iLCJzdWIiOiJ6aGFuZ3NhbiIsImV4cCI6MTY1MTg0MDQzMn0.t0sk3KB_2yTByj_0_MVQDTLikjiIGMqzx3JB8wlgI7w";
        String key = "mustbehaveenoughsizeforthekey39028234567891";
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        System.out.println(JSON.toJSONString(claims));
    }
}
