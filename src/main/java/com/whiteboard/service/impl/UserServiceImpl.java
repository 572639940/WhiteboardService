package com.whiteboard.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.whiteboard.common.excetion.BusinessException;
import com.whiteboard.config.JwtTokenUtil;
import com.whiteboard.dao.UserMapper;
import com.whiteboard.model.domain.UserDO;
import com.whiteboard.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className UserServiceImpl
 * @since 2022/11/6 17:59
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @PostConstruct
    public void a(){
        String encode = passwordEncoder.encode("123456");

        UserDO userDO = userMapper.selectById(111);
        userDO.setPassword(encode);
        userMapper.updateById(userDO);
    }

    @Override
    public UserDO getAdminByUsername(String username) {

        return userMapper.selectOne(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getUsername, username));
    }

    @Override
    public Map<String, Object> login(String username, String password) throws BusinessException {
        Assert.hasText("username", "username must not be null");
        Assert.hasText("password", "password must not be null");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null == userDetails || !passwordEncoder.matches(password, userDetails.getPassword())) {

            throw new BusinessException("account or password error !");
        }
        // 更新security登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, Object> tokeMap = new HashMap<>();
        tokeMap.put("token", token);
        tokeMap.put("tokenHead", tokenHead);

        return tokeMap;
    }
}
