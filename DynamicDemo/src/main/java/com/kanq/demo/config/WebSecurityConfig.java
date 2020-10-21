package com.kanq.demo.config;

import com.kanq.demo.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @Description:  自定义Security配置类
 * @Date: 2020-09-29 16:46
 * @Author: yyc
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
    /**
     * 权限配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置策略
        http.cors().and().csrf().disable()
                // 所有的人都可以访问的路径
                .authorizeRequests().antMatchers("/", "/static/**").permitAll().anyRequest().authenticated()
                // VIP1的用户可以访问level1下的所有路径
                .antMatchers("/level1/**").hasRole("VIP1")
                // VIP2的用户可以访问level2下的所有路径
                .antMatchers("/level2/**").hasRole("VIP2")
                 // VIP3的用户可以访问level3下所有的路径
                .antMatchers("/level3/**").hasRole("VIP3");

        http .formLogin().usernameParameter("user")
                .passwordParameter("pwd")
                .loginPage("/login").permitAll()
                .and()
                .logout().permitAll().invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .and().sessionManagement().maximumSessions(10).expiredUrl("/login");
    }

    /**
     * 自定义认证数据源，并将密码加密
     * @param auth
     * @throws Exception
     */
    @Override
    @Bean
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsServiceImpl()).passwordEncoder(new BCryptPasswordEncoder());
    }

}
