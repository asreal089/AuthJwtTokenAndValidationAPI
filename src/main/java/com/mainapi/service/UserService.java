package com.mainapi.service;

import com.mainapi.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}