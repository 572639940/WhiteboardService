package com.whiteboard.model.form;

import lombok.Data;

/**
 * <p>
 * 登陆用户信息
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className AuthUserForm
 * @since 2022/11/8 00:57
 */
@Data
public class AuthUserForm {

    private String username;

    private String password;

}
