package com.example.natterapi.exception;

import com.example.natterapi.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.example.natterapi.util.HttpServletUtils.getHeadersInfo;
import static com.example.natterapi.util.HttpServletUtils.getRequestBodyAsString;
import static java.lang.String.format;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String UUID_REF = "Ref: ";

    @Autowired
    private MessageSource messageSource;


    /**
     * A single place to customize the response body of all Exception types happening at the DispatcherServlet level.
     *
     * @param exception the exception
     * @param body      the body for the response
     * @param headers   the headers for the response
     * @param status    the response status
     * @param request   the current request
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, @Nullable Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        String uuid = generateUUId();
        log.error(buildRequestLogMessage((HttpServletRequest) ((ServletWebRequest) request).getNativeRequest(), uuid), exception);

        ErrorResponse errorMessage = ErrorResponse.builder().errorCode(ExceptionCode.INTERNAL_ERROR.name())
                .message(exception.getMessage())
                .reference(uuid)
                .build();

        return new ResponseEntity<>(buildErrorRS(status.name(), uuid, exception.getMessage()), headers, status);
    }

    /**
     * It handles RuntimeException. It logs required details and returns it a customer {@link ErrorRS} message.
     *
     * @param exception {@link RuntimeException}
     * @param request   http servlet request
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(HttpServletRequest request, RuntimeException exception) {
        return processInternalServerError(request, exception);
    }

    private ResponseEntity<ErrorResponse> processInternalServerError(HttpServletRequest request, Throwable exception) {
        String uuid = generateUUId();
        log.error(buildRequestLogMessage(request, uuid), exception);
        ExceptionCode exceptionCode = ExceptionCode.INTERNAL_ERROR;
        String argument = exception.getMessage() == null ? "" : exception.getMessage();
        String[] args = {argument};
        ErrorResponse errorRS = buildErrorMessageRS(exceptionCode.getPropertyName(), exceptionCode.name(), args, uuid);

        return new ResponseEntity<>(errorRS, ExceptionCode.INTERNAL_ERROR.getType().getHttpStatus());
    }

    /**
     * This handles default spring errors that are part of Spring auto configuration
     * {@link org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration}.
     *
     * @param request the incorrect request instance
     * @return error message as XML-document
     */
    @RequestMapping
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
        ExceptionCode exceptionCode;
        ErrorResponse errorRS;
        int statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (HttpStatus.NOT_FOUND.value() == statusCode)
        {
            String requestUrl = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
            exceptionCode = ExceptionCode.RESOURCE_NOT_FOUND;
            errorRS = buildErrorMessageRS(exceptionCode.getPropertyName(), exceptionCode.name(), new String[]{requestUrl},
                    generateUUId());
        } else
        {
            exceptionCode = ExceptionCode.INTERNAL_ERROR;
            errorRS = buildErrorMessageRS(exceptionCode.getPropertyName(), exceptionCode.name(), new String[]{""}, generateUUId());
        }

        return new ResponseEntity<>(errorRS, exceptionCode.getType().getHttpStatus());
    }


    private ErrorResponse buildErrorRS(String exceptionType, String uuid, String errorMessage) {
        return ErrorResponse.builder()
                .message(errorMessage)
                .errorCode(exceptionType)
                .reference(uuid)
                .build();

    }

    private ErrorResponse buildErrorMessageRS(String errorPropertyName, String exceptionType, String[] args, String uuid) {
        String errorMessage = messageSource.getMessage(errorPropertyName, args, Locale.getDefault());
        return buildErrorRS(exceptionType, uuid, errorMessage);
    }

    /**
     * It handles ApplicationException. It logs required details and returns it a customer {@link ErrorRS} message.
     *
     * @param exception {@link ApplicationException}
     * @param request   http servlet request
     * @return
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(HttpServletRequest request, ApplicationException exception) {
        String uuid = generateUUId();
        log.error(buildRequestLogMessage(request, uuid), exception);
        ExceptionCode exceptionCode = exception.getExceptionCode();
        String[] args = exception.getArgs() == null ? new String[]{""} : exception.getArgs();
        ErrorResponse errorMessage = buildErrorMessageRS(exceptionCode.getPropertyName(), exceptionCode.name(), args, uuid);

        return new ResponseEntity<>(errorMessage, exceptionCode.getType().getHttpStatus());
    }

    private String generateUUId() {
        return UUID.randomUUID().toString();
    }

    private String buildRequestLogMessage(HttpServletRequest request, String uuid) {
        return "Endpoint execution resulted in exception. Ref: " + uuid +
                "The request which caused an exception: " + buildRequestInfo(request);
    }

    private String buildRequestInfo(HttpServletRequest request) {
        return format("Received [%s] request: Headers [%s] Body [%s]",
                request.getMethod(), getHeadersInfo(request), getRequestBodyAsString(request));
    }

    /**
     * It handles MethodArgumentNotValidException. It logs required details and returns it a customer {@link ErrorRS} message.
     *
     * @param exception {@link MethodArgumentNotValidException}
     * @param request   http servlet request
     * @return {@link ErrorResponse}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        log.error(exception.getMessage(), exception);
        List<String> messages = new ArrayList<>();
        ExceptionCode exceptionCode = ExceptionCode.INVALID_REQUEST;
        String uuid = generateUUId();
        log.error(buildRequestLogMessage((HttpServletRequest) ((ServletWebRequest) request).getNativeRequest(), uuid), exception);
        allErrors.forEach(objectError -> messages.add(objectError.getDefaultMessage()));

        ErrorResponse errorResponse = buildErrorMessageRS(exceptionCode.getPropertyName(), exceptionCode.name(), null, uuid);
        errorResponse.setDetails(messages);
        return new ResponseEntity<>(errorResponse, ExceptionCode.INVALID_REQUEST.getType().getHttpStatus());
    }


}

