package com.example.natterapi.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorResponse {

    private String errorCode;
    private String message;
    private List<String> details;
    private String reference;
}
