package com.example.demo.springsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @GetMapping(value = {"/","index"})
    public String index(){
        return "index";
    }

    @RequestMapping("/home")
    public String home(){
        System.out.println("Welcome: " + getLoginUser());
        return "home";
    }

    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }

    @RequestMapping("/loginSuccess")
    public String loginSuccess(){
        return "loginSuccess";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    //获取登录的用户
    private String getLoginUser() {
        String username = null;
        //SpringSecurity完成认证后，会把Authentication信息放在SecurityContextHolder中，
        //而Authentication中包含了用户的信息，因此我们可以通过它获取已经登录的用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取用户身份
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails)principal;
            username = userDetails.getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
}
