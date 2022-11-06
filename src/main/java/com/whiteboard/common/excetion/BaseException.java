package com.whiteboard.common.excetion;

import java.io.Serializable;

/**
 * <p>
 * 基本异常类
 * </p>
 *
 * @author tangz
 * @version 1.0
 * @className BaseException
 * @since 2022/11/6 15:02
 */
public class BaseException extends Exception implements Serializable {
    private static final long serialVersionUID = 6257960122414939659L;

    /**
     * 说明： 构造函数
     *
     * @since 2022/11/6 15:02
     */
    public BaseException() {
        super("Base Exception");
    }

    /**
     * 说明： 构造函数
     *
     * @param message 错误信息
     */
    public BaseException(final String message) {
        super(message);
        this.message = message;
    }

    /**
     * 说明：构造函数
     *
     * @param message 错误信息
     * @param params  信息参数
     */
    public BaseException(final String message, final Serializable... params) {
        super(message);
        this.message = message;
        this.params = params;
    }

    // 消息内容
    private String message;
    // 参数
    private Serializable[] params;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(String... params) {
        this.params = params;
    }
}