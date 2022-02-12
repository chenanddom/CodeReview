package com.itdom.config;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonMerge;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint(value = "/websocket/{userId}")
@Component
public class WebSocketEndpoint {
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发生数据
     */
    private Session session;

    /**
     * 连接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        SessionPool.sessions.put(userId, session);
    }

    /**
     * 关闭连接
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        SessionPool.sessions.remove(session.getId());
    }

    /**
     * 收到客户端的消息过后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        if ("ping".equalsIgnoreCase(message)) {
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("type", "pong");
            try {
                session.getBasicRemote().sendText(JSON.toJSONString(param));
                System.out.println("应答客户端的消息:" + JSON.toJSON(param));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            SessionPool.sendMessage(message);
        }
    }
}
