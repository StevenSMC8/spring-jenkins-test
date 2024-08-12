package com.example.services;

import com.example.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.example.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getLoggedInUser(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("loggedInUsername");

        return userRepository.findByUsername(username);
    }
}

