package com.example.natterapi.endpoint.v1;


import com.example.natterapi.domain.User;
import com.example.natterapi.exception.ApplicationException;
import com.example.natterapi.exception.ExceptionCode;
import com.example.natterapi.model.ErrorResponse;
import com.example.natterapi.model.user.UserCredentials;
import com.example.natterapi.model.user.UserRegistration;
import com.example.natterapi.repository.UserRepository;
import com.lambdaworks.crypto.SCryptUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.lambdaworks.crypto.SCryptUtil.check;

@RestController
@RequestMapping("/users")
@Api(value = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "/", notes = "POST a new user",httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Accepted"),
            @ApiResponse(code = 400, message = "Validation Error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)})
    @PostMapping
    public ResponseEntity createNewUser(@Valid @RequestBody UserRegistration userRegistration) {

        Optional<User> user = userRepository.findById(userRegistration.getUserName());
        if (user.isPresent())
        {
            throw new ApplicationException(ExceptionCode.USER_ALREADY_EXISTS, "User name already used");
        }
        userRepository.save(mapUser(userRegistration));
        return ResponseEntity.accepted().build();
    }

    @ApiOperation(value = "/", notes = "POST user auth", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "Forbidden", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Validation Error", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorResponse.class)})
    @PostMapping("/auth")
    public ResponseEntity userAuth(@Valid @RequestBody UserCredentials userCredentials) {

        Optional<User> user = userRepository.findById(userCredentials.getUserName());

        if (user.isPresent()) {
            if (check(userCredentials.getPassword(),user.get().getProtectedPassword())) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private User mapUser(UserRegistration userRegistration) {
        return User.builder()
                .userName(userRegistration.getUserName())
                .createdTime(LocalDateTime.now())
                .emailAddress(userRegistration.getEmailAddress())
                .protectedPassword(hashPassword(userRegistration.getPassword()))
                .name(userRegistration.getName())
                .build();
    }

    private String hashPassword(String password){
       return SCryptUtil.scrypt(password, 32768, 8, 1);
    }
}
