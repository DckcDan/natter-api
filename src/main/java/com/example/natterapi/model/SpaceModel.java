package com.example.natterapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceModel {
    private Long id;
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 50)
    private String name;
    @NotBlank(message = "Owner is mandatory")
    @Size(min = 3, max = 50)
    private String owner;
    private LocalDate createdDate;

}