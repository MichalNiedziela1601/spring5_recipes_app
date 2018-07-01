package com.example.spring5recipes.services;

import com.example.spring5recipes.domain.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User save(User user);

    void delete(Long id);
}
