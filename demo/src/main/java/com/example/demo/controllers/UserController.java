package com.example.demo.controllers;

import com.example.demo.services.UserService;
import com.example.demo.models.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "User")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public void addUser(@RequestBody User user){
        userService.addUser(user);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") Integer id) {
        userService.deleteUser(id);
    }
}

