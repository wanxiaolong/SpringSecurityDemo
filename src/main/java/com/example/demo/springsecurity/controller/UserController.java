package com.example.demo.springsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @GetMapping(value = {"/","index"})
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/register")
    public String regist(){
        return "register";
    }

    @RequestMapping("/home")
    public String home(){
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
}
