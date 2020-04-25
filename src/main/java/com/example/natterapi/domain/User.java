package com.example.natterapi.domain;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document("User")
@Data
@Builder
public class User {
    @Id
    private String userName;
    private String name;
    private String emailAddress;
    private LocalDateTime createdDate;
    private LocalDateTime lastLoggedIn;
    //user block, how many times faied logging.
    private String protectedPassword;
    private Boolean active;

    @DBRef
    private Set<Role> roles;
}
