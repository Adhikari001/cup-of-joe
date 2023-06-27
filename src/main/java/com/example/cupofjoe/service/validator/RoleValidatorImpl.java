package com.example.cupofjoe.service.validator;

import com.example.cupofjoe.comms.exceptionhandler.RestException;
import com.example.cupofjoe.entity.Role;
import com.example.cupofjoe.repository.RoleRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleValidatorImpl implements RoleValidator {
    private final RoleRepository roleRepository;

    public RoleValidatorImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.orElseThrow(()->new RestException("RV001", "Role id is not valid."));
    }
}
