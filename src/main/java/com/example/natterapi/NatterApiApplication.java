package com.example.natterapi;

import com.example.natterapi.domain.Role;
import com.example.natterapi.domain.User;
import com.example.natterapi.domain.UserRole;
import com.example.natterapi.repository.RoleRepository;
import com.example.natterapi.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;

@SpringBootApplication
public class NatterApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NatterApiApplication.class, args);
    }

    /**
     * Just for development to init MongoStore with roles and dummy users.
     */
    @Bean
    InitializingBean initializer(RoleRepository roleRepository, UserService userService){
        return () -> {
            roleRepository.save(Role.builder().id("1").role(UserRole.USER.name()).build());
            roleRepository.save(Role.builder().id("2").role(UserRole.ENDPOINT_ADMIN.name()).build());
            roleRepository.save(Role.builder().id("3").role(UserRole.ADMIN.name()).build());
            HashSet userRole = new HashSet();
            userRole.add(UserRole.USER.name());
            HashSet adminRole = new HashSet();
            userRole.add(UserRole.ADMIN.name());
            userService.save(User.builder().userName("daniel").protectedPassword("pass").roles(userRole).build());
            userService.save(User.builder().userName("admin").protectedPassword("pass").roles(adminRole).build());
        };
    }
}
