package com.coding.blog.web.security.config;

import com.coding.blog.service.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @User Administrator
 * @CreateTime 2023/12/6 14:24
 * @className com.coding.blog.web.security.config.OrtherSecurityConfig
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OtherSecurityConfig extends PearlWebSecurityConfig{
    // @Autowired
    private IUsersService usersService;
    @Autowired
    public void setUsersService(@Lazy IUsersService usersService){
        this.usersService = usersService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return userLogin -> usersService.loadUserByUsername(userLogin);
    }

}
