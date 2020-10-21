package com.kanq.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 87990
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        String pwd = create("123");
       List<String> roleList = null; //userRoleMapper.selectByUserName(userName);
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>() ;

        if (roleList != null && roleList.size()>0){
            for (String role : roleList){
                grantedAuthorityList.add(new SimpleGrantedAuthority(role)) ;
            }
        }
        return new User(userName,pwd,grantedAuthorityList);
    }

    /**
     * 对用户的密码进行加密
     * @param password  密码
     * @return
     */
    public String create(String password) {
        //进行加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String sysUser =  encoder.encode(password.trim());
        return sysUser;
    }
}
