package com.itdom.config;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionPool {
    public static Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

    public static void close(String sessionId) throws IOException {

        Set<Map.Entry<String, Session>> entries = sessions.entrySet();
        for (Map.Entry<String, Session> entry : entries) {
            Session value = entry.getValue();
            if (value.getId().equals(sessionId)) {
                value.close();
                sessions.remove(entry.getKey());
            }
        }
//        Session session = sessions.get(sessionId);
//        if (session != null) {
//            sessions.get(sessionId).close();
//        }
    }

    public static void sendMqessage(String sessionId, String message) {
        sessions.get(sessionId).getAsyncRemote().sendText(message);
    }

    public static void sendMessage(String message) {
        for (String sessionId : SessionPool.sessions.keySet()) {
            SessionPool.sessions.get(sessionId).getAsyncRemote().sendText(message);
        }
    }
}
