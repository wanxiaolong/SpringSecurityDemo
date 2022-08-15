package com.example.demo.springsecurity.service;

import com.example.demo.springsecurity.domain.User;
import com.example.demo.springsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findBy(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findBy(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
