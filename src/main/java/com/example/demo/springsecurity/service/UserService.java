package com.example.demo.springsecurity.service;

import com.example.demo.springsecurity.domain.User;

public interface UserService {
    User findBy(String username, String password);
}
