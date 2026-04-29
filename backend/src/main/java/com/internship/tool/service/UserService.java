package com.internship.tool.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.internship.tool.entity.User;
import com.internship.tool.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

