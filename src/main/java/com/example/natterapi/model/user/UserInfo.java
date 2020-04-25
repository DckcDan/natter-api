package com.example.natterapi.model.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserInfo {

    private String userName;
    private LocalDateTime lastLoggedIn;
    private LocalDateTime createdDate;
    private Boolean active;
}
