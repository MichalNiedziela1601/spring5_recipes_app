package com.example.spring5recipes.repositories;

import com.example.spring5recipes.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {

    User findByUsername(String name);
}
