package com.whiteboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * <p>
 *
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className WebSocketConfig
 * @since 2022/11/8 01:41
 */
@Configuration
public class WebSocketConfig {

    /**
     * <p>
     * 注入Bean对象并扫描注解了@ServerEndpoint注解的类
     * </p>
     * 
     * @version 1.0
     * @return serverEndpointExporter
     * @since 2022/11/8
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
