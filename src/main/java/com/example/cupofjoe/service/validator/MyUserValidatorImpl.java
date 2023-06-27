package com.example.cupofjoe.service.validator;

import com.example.cupofjoe.comms.exceptionhandler.RestException;
import com.example.cupofjoe.entity.MyUser;
import com.example.cupofjoe.repository.MyUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserValidatorImpl implements MyUserValidator {
    private final MyUserRepository userRepository;

    public MyUserValidatorImpl(MyUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public MyUser validateMyUser(String userId) {
        Optional<MyUser> optionalMyUser = userRepository.findById(userId);
        if(optionalMyUser.isEmpty())
            throw new RestException("UV001", "User id is not valid");
        MyUser myUser = optionalMyUser.get();
        if(myUser.isDisabled())
            throw new RestException("UV002", "User has been disabled.");
        return myUser;
    }
}
