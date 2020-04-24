package com.example.natterapi.security;


import com.example.natterapi.domain.Role;
import com.example.natterapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MongoUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.example.natterapi.domain.User> user = userRepository.findById(username);
        if(!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
         return new User(user.get().getUserName(), user.get().getProtectedPassword(), getUserAuthority(user.get().getRoles()));
    }


    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
      return  userRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.getRole()))
                .collect(Collectors.toList());
    }


}