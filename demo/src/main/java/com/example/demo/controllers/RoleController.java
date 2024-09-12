package com.example.demo.controllers;

import com.example.demo.models.Role;
import com.example.demo.services.RoleService;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "Role")
public class RoleController {

    private final RoleService roleService;


    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public void addRole(Role role){
        roleService.addRole(role);
    }

    @DeleteMapping(path = "{roleId}")
    public void deleteRole(@PathVariable("roleId") Integer id){
        roleService.deleteRole(id);
    }
}
