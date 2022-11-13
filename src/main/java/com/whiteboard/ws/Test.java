package com.whiteboard.ws;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className Test
 * @since 2022/11/8 01:37
 */
@ServerEndpoint(value = "/ws/chat/{userid}")
@Component
public class Test {
    /**
     * 用来储存在线用户的容器
     */
    public static Map<Integer, Test> onlineUsers = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userid") Integer userId, EndpointConfig endpointConfig) {
        System.out.println("aaa");
        onlineUsers.put(userId,this);
    }

}
