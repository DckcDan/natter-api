package com.example.natterapi.model.user;

import lombok.Data;

@Data
public class UserRegistration {
    private String userName;
    private String emailAddress;
    private String name;
    private String password;
}
