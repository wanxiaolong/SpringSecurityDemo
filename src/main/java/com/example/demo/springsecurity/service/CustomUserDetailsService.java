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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义认证方案是，这个Bean会被注入到SecurityConfig中，
 * 然后被绑定到AuthenticationManagerBuilder对象上。
 * Spring Security通过这个loadUserByUsername来查询用户和用户的权限。
 */
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

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

        //查找用户的所有权限
        List<String> authorities = userRepository.findAuthoritiesBy(username);

        //权限authority
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(authorities)) {
            grantedAuthorities = authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        //用User.builder.roles()方法，其实是用每个role，加上前缀"ROLE_"后，再生成一个Authority对象
        //其作用和User.builder.authorities()是一样的，最后都是生成权限对象，只是用role转成的权限多了个前缀"ROLE_"。
        //参看roles()方法的源码
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(userInDB.getPassword())
                //使用roles来认证，需要在SecurityConfig中以.hasAnyRole()或者.hasRole()来设置角色
                //注意：role转换成Authority的时候，role不能以"ROLE_"开头，因为这个前缀会被自动添加。请参看roles()的源码。
                //.roles(roles)
                //使用authorities来认证，需要在SecurityConfig中以.hasAnyAuthority或者.hasAuthority()来设置权限
                .authorities(grantedAuthorities)
                .build();
    }
}
