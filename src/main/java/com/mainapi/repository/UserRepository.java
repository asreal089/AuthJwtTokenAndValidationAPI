package com.mainapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mainapi.model.User;

public interface UserRepository extends JpaRepository<User, String>{
    User findByUsername(String username);
}
