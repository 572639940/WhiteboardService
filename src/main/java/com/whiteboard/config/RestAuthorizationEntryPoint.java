package com.whiteboard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whiteboard.common.api.vo.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * 当未登录或者token失效时访问接口时，自定义的返回结果
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className RestAuthorizationEntryPoint
 * @since 2022/11/6 18:31
 */
@Component
public class RestAuthorizationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Result<?> result = Result.error("尚未登录，请登录！");
        result.setCode(401);
        out.write(new ObjectMapper().writeValueAsString(result));
        out.flush();
        out.close();
    }
}