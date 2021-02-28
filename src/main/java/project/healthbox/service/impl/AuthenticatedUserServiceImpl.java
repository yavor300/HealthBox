package project.healthbox.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import project.healthbox.service.AuthenticatedUserService;

import java.util.List;

@Component
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {
    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public List<String> getRoles() {
        return null;
    }
}