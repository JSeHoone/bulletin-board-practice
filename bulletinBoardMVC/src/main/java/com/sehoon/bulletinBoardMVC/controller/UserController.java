package com.sehoon.bulletinBoardMVC.controller;

import com.sehoon.bulletinBoardMVC.model.entity.UserEntity;
import com.sehoon.bulletinBoardMVC.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(HttpSession session, @RequestParam(name = "email") String email, @RequestParam(name="password") String password){

        boolean isUser = userService.checkUser(email, password);
        if (!isUser) {
            return "/error/loginError";
        }
        UserEntity loginUser = userService.getUser(email);

        session.setAttribute("userId", loginUser.getUserId());
        return "redirect:/posts/list";
    }
}
