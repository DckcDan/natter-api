package com.example.natterapi.domain;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("users")
@Data
@Builder
public class User {
    @Id
    private String userName;
    private String name;
    private String emailAddress;
    private LocalDateTime createdTime;
    private LocalDateTime lastLoggedIn;
    //user block, how many times faied logging.
    private String protectedPassword;
}
