package com.test.authorization.service;

import com.test.authorization.dto.SignUpDto;
import com.test.authorization.model.User;

import java.util.List;

public interface UserService {

    User register(SignUpDto signUpDto);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);

}
