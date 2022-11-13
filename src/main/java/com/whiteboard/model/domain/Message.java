package com.whiteboard.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 浏览器发送给服务器的websocket数据.
 * @author tangzb
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    /** 接收方*/
    private String toName;
    /** 发送的信息*/
    private String message;
}
