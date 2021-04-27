package com.example.demo.springsecurity.service;

import com.example.demo.springsecurity.domain.User;
import com.example.demo.springsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义认证方案是，这个Bean会被注入到SecurityConfig中，
 * 然后被绑定到AuthenticationManagerBuilder对象上。
 * Spring Security通过这个loadUserByUsername来查询用户和用户的权限。
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private GrantedAuthority DEFAULT_ROLE = new SimpleGrantedAuthority("USER");

    @Autowired
    private UserRepository userRepository;

    /**
     * 可以参考JdbcDaoImpl中的默认实现loadUserByUsername
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userInDB = userRepository.findByUsername(username);
        if (userInDB == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String dbRole = userInDB.getRole();
        if (!StringUtils.hasLength(dbRole)){
            //默认的用户角色
            grantedAuthorities.add(DEFAULT_ROLE);
        } else {
            String [] roles = dbRole.split(",");
            for (String r : roles){
                //从数据库中查询到的用户角色，用每个角色创建一个SimpleGrantedAuthority对象。
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(r);
                grantedAuthorities.add(grantedAuthority);
            }
        }

        return new org.springframework.security.core.userdetails.User(
                username, userInDB.getPassword(), grantedAuthorities);
    }
}
