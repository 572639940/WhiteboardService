package com.whiteboard.service;

import com.whiteboard.common.excetion.BusinessException;
import com.whiteboard.model.domain.UserDO;
import java.util.Map;

/**
 * <p>
 * 用户service
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className IUserService
 * @since 2022/11/6 17:58
 */
public interface IUserService {

    /**
     * <P>
     * 通过用户账号查找用户信息
     * </P>
     * 
     * @param username 账号
     * @return user
     */
    UserDO getAdminByUsername(String username);

    /**
     * <p>
     * 用户登陆
     * </p>
     * 
     * @return map
     */
    Map<String, Object> login(String username,String password) throws BusinessException;

}
