package com.whiteboard.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务端发送给客户端的消息.
 * 
 * @author tangzb
 * @version 1.0
 * @className ResultMessageVO
 * @since 2022/11/13 14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultMessageVO {
    /**
     * 是否是系统消息
     */
    private boolean systemMsgFlag;

    /**
     * 发送方Name
     */
    private String fromName;

    /**
     * 发送的数据
     */
    private Object message;

}