package com.lab.service;

import com.lab.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    User save(User user);
    User create(User user);
    User getByUsername(String username);
    UserDetailsService userDetailsService();
    User getCurrentUser();
}
