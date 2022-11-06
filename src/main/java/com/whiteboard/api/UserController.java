package com.whiteboard.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户api
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className UserController
 * @since 2022/11/6 18:00
 */
@RestController()
@RequestMapping("api/user")
@Api(tags = "用户管理")
public class UserController {

    @RequestMapping("/aaa")
    @ApiOperation(value = "测试 swwager")
    public void test(){

    }



}
