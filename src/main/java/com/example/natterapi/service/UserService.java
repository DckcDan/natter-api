package com.example.natterapi.service;

import com.example.natterapi.domain.Role;
import com.example.natterapi.domain.User;
import com.example.natterapi.domain.UserRole;
import com.example.natterapi.repository.RoleRepository;
import com.example.natterapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user){
        user.setCreatedTime(LocalDateTime.now());
        Role userRole = roleRepository.findByRole(UserRole.USER.name());
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        user.setActive(Boolean.TRUE);
        user.setProtectedPassword(passwordEncoder.encode(user.getProtectedPassword()));
        userRepository.save(user);
        return user;
    }

    public Optional<User> findById(String userName) {
        return userRepository.findById(userName);
    }


}
