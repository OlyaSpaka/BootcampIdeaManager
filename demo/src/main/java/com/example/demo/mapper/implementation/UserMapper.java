package com.example.demo.mapper.implementation;

import com.example.demo.dto.input.InputUserDTO;
import com.example.demo.dto.output.OutputUserDTO;
import com.example.demo.mapper.interf.UserMapperInt;
import com.example.demo.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper implements UserMapperInt {
    @Override
    public OutputUserDTO map(User user){
        if (user == null) return null;

        OutputUserDTO userDTO = new OutputUserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());


        return userDTO;
    }

    @Override
    public User map(InputUserDTO userDTO){
        if (userDTO == null) return null;

        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());

        return user;
    }
}

