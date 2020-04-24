package com.example.natterapi.model.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserCredentials {
    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;
}
