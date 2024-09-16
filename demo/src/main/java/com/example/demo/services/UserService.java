package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void addUser(User user) {
        userRepository.save(user);
    }
    public void deleteUser(Integer id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("User with Id " + id + " does not exist");
        } else {
            userRepository.deleteById(id);
        }
    }
//    public User getCurrentUser() { // todo: move to controller
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        return (User) auth.getPrincipal();
//    }
}
