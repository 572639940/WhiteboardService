package com.whiteboard.common.constant;

/**
 * <p>
 *
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className CommonConstant
 * @since 2022/11/6 18:38
 */
public interface CommonConstant {

    /** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    Integer SC_OK_200 = 200;

    /** 访问权限认证未通过 510 */
    Integer SC_NO_AUTHZ = 510;

}
