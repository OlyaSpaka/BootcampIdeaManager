package com.example.demo.services;

import com.example.demo.models.User;
import org.springframework.stereotype.Service;
import com.example.demo.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {this.userRepository = userRepository;}

    public void addUser(User user){

        userRepository.save(user);

    }
    public void deleteUser(Integer id){
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("User with Id " + id + " does not exist");
        } else {
            userRepository.deleteById(id);
        }
    }
}
