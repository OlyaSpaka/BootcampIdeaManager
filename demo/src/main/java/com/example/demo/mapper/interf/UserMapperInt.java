package com.example.demo.mapper.interf;

import com.example.demo.dto.input.InputUserDTO;
import com.example.demo.dto.output.OutputUserDTO;
import com.example.demo.models.User;

public interface UserMapperInt {
    OutputUserDTO map(User user);
    User map(InputUserDTO userDTO);
}
