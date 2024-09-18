package com.example.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("hasRole('ROLE_Admin')")
@RequestMapping("/admin")
@Controller
public class AdminToolsController {

    @GetMapping
    public String getAdminToolsView() {
        return "admin-tools";
    }


}
