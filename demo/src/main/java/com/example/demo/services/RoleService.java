package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRole(Role role){
        roleRepository.save(role);
    }

    public void deleteRole(Integer id){
        boolean exists = roleRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Role with Id " + id + " does not exist");
        } else {
            roleRepository.deleteById(id);
        }
    }
}
