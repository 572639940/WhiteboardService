package com.whiteboard.config;

import com.whiteboard.model.domain.UserDO;
import com.whiteboard.service.IUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * <p>
 *
 * </p>
 *
 * @author tangzb
 * @version 1.0
 * @className SecurityConfig
 * @since 2022/11/6 18:29
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private IUserService userService;
    @Resource
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/api/auth/**",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/chat",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha",
                "/ws/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ??????JWT????????????csrf
        http.csrf().disable()
            // ??????token????????????session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
            .anyRequest().authenticated().and()
            // ????????????
            .headers().cacheControl();
        // ??????jwt ?????????????????????
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // ????????????????????????????????????????????????
        http.exceptionHandling().accessDeniedHandler(restfulAccessDeniedHandler)
            .authenticationEntryPoint(restAuthorizationEntryPoint);
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserDO user = userService.getAdminByUsername(username);

            return ObjectUtils.isNotEmpty(user) ? user : null;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new MyPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {

        return new JwtAuthenticationTokenFilter();
    }


}
