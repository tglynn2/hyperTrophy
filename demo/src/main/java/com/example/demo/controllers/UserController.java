package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.User;
import com.example.demo.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired 
    UserService userService;

    @PostMapping("/create")
    public User createUser(@RequestParam(required = true) String username){
        User user = new User();
        user.setUsername(username);
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestParam(required = true) String username, HttpServletRequest request){
        User user = userService.getUserByUsername(username).orElse(null);
        if(user != null){
            request.getSession().setAttribute("id", user.getId());
        }
        return user;
    }
    
}
