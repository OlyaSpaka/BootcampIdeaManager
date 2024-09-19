package com.example.demo.mapper.implementation;

import com.example.demo.dto.input.InputUserDTO;
import com.example.demo.dto.output.OutputUserDTO;
import com.example.demo.models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void testMapUserToOutputUserDTO() {
        User user = new User();
        user.setId(1);
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("securepassword");

        OutputUserDTO userDTO = userMapper.map(user);

        assertNotNull(userDTO);
        assertEquals(1, userDTO.getId());
        assertEquals("john_doe", userDTO.getUsername());
    }

    @Test
    void testMapInputUserDTOToUser() {
        InputUserDTO userDTO = new InputUserDTO();
        userDTO.setUsername("john_doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("securepassword");

        User user = userMapper.map(userDTO);

        assertNotNull(user);
        assertNull(user.getId()); // ID should be null as it's not part of InputUserDTO
        assertEquals("john_doe", user.getUsername());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("securepassword", user.getPassword());
    }

    @Test
    void testMapUserToOutputUserDTOWithNullInput() {
        OutputUserDTO userDTO = userMapper.map((User) null);

        assertNull(userDTO);
    }

    @Test
    void testMapInputUserDTOToUserWithNullInput() {
        User user = userMapper.map((InputUserDTO) null);

        assertNull(user);
    }
}
