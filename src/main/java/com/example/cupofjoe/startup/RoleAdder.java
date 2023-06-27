package com.example.cupofjoe.startup;

import com.example.cupofjoe.entity.Role;
import com.example.cupofjoe.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleAdder {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleAdder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void addRole(){
        List<Role> roles = roleRepository.findAll();
        if(!roles.isEmpty()) return;
        Role role = new Role();
        role.setName("DEFAULT");
        roleRepository.save(role);
    }
}
