package com.whiteboard.ws;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * 用来获取HttpSession对象.
 * @author 朕爷专属pro
 */
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 获取httpsession对象
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        // 将httpsession对象存储到配置对象中
        Map<String, Object> userProperties = sec.getUserProperties();
        userProperties.put(HttpSession.class.getName(), httpSession);
    }
}
