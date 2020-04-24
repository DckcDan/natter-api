package com.example.natterapi.endpoint.v1;


import com.example.natterapi.domain.Role;
import com.example.natterapi.domain.User;
import com.example.natterapi.domain.UserRole;
import com.example.natterapi.exception.ApplicationException;
import com.example.natterapi.exception.ExceptionCode;
import com.example.natterapi.model.ErrorResponse;
import com.example.natterapi.model.user.UserRegistration;
import com.example.natterapi.repository.UserRepository;
import com.example.natterapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;


@RestController
@RequestMapping("/v1/users")
@Api(value = "/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "/", notes = "POST a new user",httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Accepted"),
            @ApiResponse(code = 400, message = "Validation Error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)})
    @PostMapping
    public ResponseEntity createNewUser(@Valid @RequestBody UserRegistration userRegistration) {

        Optional<User> user = userService.findById(userRegistration.getUserName());
        if (user.isPresent())
        {
            throw new ApplicationException(ExceptionCode.USER_ALREADY_EXISTS, "User name already used");
        }
        userService.save(mapUser(userRegistration));
        return ResponseEntity.accepted().build();
    }


    private User mapUser(UserRegistration userRegistration) {
        return User.builder()
                .userName(userRegistration.getUserName())
                .emailAddress(userRegistration.getEmailAddress())
                .protectedPassword(userRegistration.getPassword()) //service layer will hash it.
                .name(userRegistration.getName())
                .build();
    }

    private Role createUserRole(){
        return Role.builder().role(UserRole.USER.name()).build();
    }
}


