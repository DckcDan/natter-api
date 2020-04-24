package com.example.natterapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ExceptionCode {

    SPACE_NOT_FOUND(Type.NOT_FOUND),
    INTERNAL_ERROR(Type.INTERNAL_SERVICE),
    INVALID_REQUEST(Type.VALIDATION),
    USER_ALREADY_EXISTS(Type.VALIDATION),
    RESOURCE_NOT_FOUND(Type.NOT_FOUND);


    private Type type;

    public String getPropertyName() {

        return type.getName() + "." + this.name();
    }

    /**
     * Type (or category) of error.
     */
    @Getter
    @AllArgsConstructor
    public enum Type {
        VALIDATION(HttpStatus.BAD_REQUEST),
        NOT_FOUND(HttpStatus.NOT_FOUND),
        PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE),
        INTERNAL_SERVICE(HttpStatus.INTERNAL_SERVER_ERROR);

        private HttpStatus httpStatus;

        public String getName()
        {
            return this.name();
        }

    }

}
