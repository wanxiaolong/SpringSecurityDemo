package com.example.demo.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //自定义的认证方式需要用到这个Bean
    @Autowired
    private UserDetailsService userDetailsService;

    //JDBC认证的时候需要用到这个Bean
    @Autowired
    private DataSource dataSource;

    /**
     * 演示三种方式进行用户认证
     * 1.基于配置在内存中的用户进行认证
     * 2.基于数据库的用户认证
     * 3.自定义的用户认证，需要实现UserDetailsService接口
     *
     * 示例中，三种认证方式配置的用户都一样，三种方式选一种即可。
     * 当然，一个web项目中可以有不止一种认证方式。比如这里就可以1和3共存，或者1和2共存，都可以。
     *
     * <br/>
     * 注意，当多种认证方式并存的时候，其中一个方式验证通过，即算通过。
     * 比如，1和3共存的时候：
     * 内存中配置了spring这个用户，密码为1，
     * DB中也配置了这个spring用户，密码为2，
     * 此时的结果是：用spring用户登录，密码为1页可以通过，密码为2也可以通过。
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用BCryptPasswordEncoder来对密码加密
        BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
        //1.使用配置在内存中的用户进行认证
        auth.inMemoryAuthentication()
                .passwordEncoder(bcryptEncoder)
                //普通用户：用户名为spring，密码为1，角色为USER.
                .withUser("spring")
                //在设置密码的时候，需要先用encoder加密
                .password(bcryptEncoder.encode("1"))
                //.authorities("USER")
                .roles("USER")
                .and()
                //管理员：用户名为admin，密码为1，角色为ADMIN+USER
                .withUser("admin")
                .password(bcryptEncoder.encode("1"))
                //.authorities("ADMIN")
                .roles("ADMIN")
        ;


        //2.基于数据库的用户认证
//        auth.jdbcAuthentication()
//                //指定数据源，在application.properties中配置了就会有
//                .dataSource(dataSource)
//
//                //注意这里的查询语句很有讲究。
//                //这两个语句在JdbcDaoImpl中都有默认的语句，但是如果表或字段跟默认的不同，就需要自定义这两个查询语句。
//
//                //usersByUsernameQuery用于通过用户名查询用户
//                //JdbcUserDetailsManager.loadUsersByUsername()会取出这个查询并执行（可以debug）
//                //然后通过mapToUser()将查询结果方法转换成一个UserDetails对象
//                //这里查询的字段要和mapToUser()里的顺序一致，列数量要一致，且列的数据类型要兼容
//                .usersByUsernameQuery("select username, password, enabled from user where username=?")
//
//                //authoritiesByUsernameQuery是用于通过用户名查询改用户的角色的
//                //JdbcDaoImpl.loadUserAuthorities()会执行这个语句，并把结果集的第2列取出来作为该用户的ROLE（可以debug）
//                //同样，这里的查询字段的顺序、数量、数据类型要一致
//                //这个过程和我们自定义的认证方式时，在CustomUserDetailsService中的实现差不多，都是用DB中取到的ROLE
//                //来封装一个Authority对象：new SimpleGrantedAuthority(roleName);
//                .authoritiesByUsernameQuery("select username, authority from user where username=?")
        ;


        //3.自定义的用户认证，需要实现UserDetailsService接口
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //为了演示方便，我们使用NoOpPasswordEncoder（这个就是不加密）
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //认证的登录页面/login，和认证成功的页面/loginSuccess
                .formLogin().loginPage("/login").successForwardUrl("/loginSuccess")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .and()
                .rememberMe().key("rememberMeKey").tokenValiditySeconds(2419200)//单位是秒
                .and()
                .authorizeRequests()
                //对这两个页面需要授权。有两种匹配策略：按角色Role和按权限Authority
                //(1). 按Authority授权。Authority代表的是"权限"，配置用户的时候，需要对用户授权。
                //以hasAuthority()或者hasAnyAuthority()的方式来判断用户是否有该权限
                //如果使用的是JDBC认证方式，则需要在CustomerUserDetailsService中对User设置.authorities()设置用户拥有的权限
                //比如：内存用户中是这么配置的：.withUser("spring").password("1").authorities("USER")
//                    .antMatchers("/home", "/loginSuccess").hasAnyAuthority("USER","ADMIN")//ADMIN和USER都可以访问
//                    .antMatchers("/admin", "/loginSuccess").hasAuthority("ADMIN")//只有ADMIN可以访问

                //(2). 按Role授权。Role代表的是"角色"，配置用户的时候，需要对用户设置角色。
                //以hasRole()或者hasAnyRole()的方式来判断用户是否具有该角色，
                //如果使用的是JDBC认证方式，则需要在CustomerUserDetailsService中对User设置.roles()设置用户拥有的角色
                .antMatchers("/home", "/loginSuccess").hasAnyRole("USER","ADMIN")
                .antMatchers("/admin", "/loginSuccess").hasRole("ADMIN")
                //其余的页面不需要授权
                .anyRequest().permitAll();
    }
}
