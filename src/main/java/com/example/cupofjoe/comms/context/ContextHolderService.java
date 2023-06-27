package com.example.cupofjoe.comms.context;

import com.example.cupofjoe.repository.MyUserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class ContextHolderService {
    private MyUserRepository userRepository;

    public ContextHolderService(MyUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Context getContext() {
        return ContextHolder.get();
    }

    public void setContext(String userId,String username, String role, List<String> permission) {
        setContextForUser(userId, username, role, permission);
    }

    private void setContextForUser(String userId, String username, String role, List<String> permission) {
        new ContextHolder(new Context(userId, username, role, permission)).run();
    }
}
