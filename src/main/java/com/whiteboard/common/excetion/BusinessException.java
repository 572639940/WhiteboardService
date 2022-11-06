package com.whiteboard.common.excetion;

/**
 * <p>
 * 业务异常
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className BusinessException
 * @since 2022/11/6 15:02
 */
public class BusinessException extends BaseException {
    private static final long serialVersionUID = 78484407421113621L;
    // 字段名
    private String fieldName;

    /**
     * 说明： 构造函数
     *
     * @param message 错误消息
     */
    public BusinessException(final String message) {
        super(message);
    }

    /**
     * 说明：构造函数
     *
     * @param message 错误信息
     * @param params  错误参数
     */
    public BusinessException(final String message, final Object... params) {
        super(message, params);
    }

    /**
     * 说明：构造函数
     *
     * @param fieldName 字段名，（字段的id）
     * @param message   错误信息
     * @param params    错误参数
     */
    public BusinessException(final String message, final String fieldName, final Object... params) {
        super(message, params);
        this.fieldName = fieldName;
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}