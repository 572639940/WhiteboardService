package com.whiteboard.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whiteboard.model.dto.MessageDTO;
import com.whiteboard.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className ChatEndpoint
 * @since 2022/11/13 14:30
 */
@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
@Component
@Slf4j
public class ChatEndpoint {

    /**
     * 用来储存在线用户的容器
     */
    public static Map<String, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();
    /**
     * 存储在线房间
     */
    public static Map<String, ChatEndpoint> onlineRooms = new ConcurrentHashMap<>();

    /**
     * 用来给客户端发送数据
     */
    private Session session;

    /**
     * 用来获取在登录成功后，放在httpsession域中存放的username
     */
    private HttpSession httpSession;

    /*建立时调用*/
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        log.info("websocket 建立连接onOpen!");
        this.session = session;
        // 获取httpsession对象
        HttpSession httpSession = (HttpSession)endpointConfig.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;
        // 获取httpsession域中存放的username对应的值
        String username = (String)httpSession.getAttribute("username");
        // 存放到onlineUsers中保存
        onlineUsers.put(username, this);
        // 系统消息推送所有在线用户给客户端
        // 封装系统推送消息,前端onmessage接收的数据
        String message = MessageUtils.getMessage(true, null, getAllOnlineUsername());
        broadcastMsgToAllOnlineUsers(message);
    }

    /**
     * @param message 给客户端发送消息
     * @return void
     */
    private void broadcastMsgToAllOnlineUsers(String message) {
        // 所有登录用户名称
        Set<String> names = onlineUsers.keySet();
        for (String name : names) {
            ChatEndpoint chatEndpoint = onlineUsers.get(name);
            // 获取推送对象
            RemoteEndpoint.Basic basicRemote = chatEndpoint.session.getBasicRemote();
            try {
                basicRemote.sendText(message);
            } catch (IOException e) {
                log.error("广播发送系统消息失败！{}", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取发送给客户端的消息，消息数据示例值：{“systemMsgFlag”: true, "fromName": null, "message": ["Name1", "Name2"]}.
     *
     * @return java.lang.Object
     */
    private Object getAllOnlineUsername() {
        return ChatEndpoint.onlineUsers.keySet();
    }

    /**
     * 接收到客户端发送的数据时调用.
     *
     * @param message 客户端发送的数据
     * @param session session对象
     * @return void
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            MessageDTO msg = objectMapper.readValue(message, MessageDTO.class);
            // 获取接收信息的用户
            String toName = msg.getToName();
            // 获取发送的消息
            String msgData = msg.getMessage();
            // 获取当前登录的用户
            String username = (String)httpSession.getAttribute("username");

            // 发送消息
            onlineUsers.get(toName).session.getBasicRemote().sendText(msgData);
        } catch (JsonProcessingException e) {
            log.error("接收客户端的消息，转换出错了！");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭时调用
     */
    @OnClose
    public void onClose(Session session) {
        String username = (String)httpSession.getAttribute("username");
        //
        ChatEndpoint remove = onlineUsers.remove(username);
        // 广播
        // String message = MessageUtils.getMessage(true, null, getAllOnlineUsername());
        // broadcastMsgToAllOnlineUsers(message);
    }
}
