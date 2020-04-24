package com.example.natterapi.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document("Space")
@Builder
public class Space {
    @Transient
    public static final String SEQUENCE_NAME = "space_sequence";
    @Id
    private Long id;
    private String name;
    private String userName;
    private LocalDate createdDate;

}
