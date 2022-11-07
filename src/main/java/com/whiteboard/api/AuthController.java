package com.whiteboard.api;

import com.whiteboard.common.excetion.BusinessException;
import com.whiteboard.model.form.AuthUserForm;
import com.whiteboard.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 认证
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className AuthController
 * @since 2022/11/7 15:40
 */
@RequestMapping("/api/auth")
@RestController
@Api(tags = "系统认证")
public class AuthController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "登陆")
    @RequestMapping("/login")
    public Map<String, Object> login(AuthUserForm authUserForm) throws BusinessException {
        return userService.login(authUserForm.getUsername(), authUserForm.getPassword());
    }

}
