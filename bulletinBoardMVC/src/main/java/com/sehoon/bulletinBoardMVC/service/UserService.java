package com.sehoon.bulletinBoardMVC.service;

import com.sehoon.bulletinBoardMVC.model.entity.UserEntity;
import com.sehoon.bulletinBoardMVC.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkUser(String email, String password) {
        UserEntity findUser = userRepository.findByEmail(email);

        if (findUser == null) {
            return false;
        }

        if (!findUser.getPassword().equals(password)) {
            return false;
        }

        return true;
    }

    public UserEntity getUser(String email) {
        UserEntity findUser = userRepository.findByEmail(email);
        return findUser;
    }

}
