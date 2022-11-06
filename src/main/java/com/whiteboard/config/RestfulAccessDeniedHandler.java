package com.whiteboard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whiteboard.common.api.vo.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * 当访问接口没有权限时，自定义返回结果
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className RestfulAccessDeniedHandler
 * @since 2022/11/6 18:31
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Result<?> result = Result.error("权限不足");
        result.setCode(403);
        out.write(new ObjectMapper().writeValueAsString(result));
        out.flush();
        out.close();
    }
}