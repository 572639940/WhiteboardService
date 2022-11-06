package com.whiteboard.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className MyPasswordEncoder
 * @since 2022/11/6 18:45
 */
public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {

        return DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex(charSequence.toString().getBytes())).getBytes());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {

        return s.equals(
            DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex(charSequence.toString().getBytes())).getBytes()));
    }
}
