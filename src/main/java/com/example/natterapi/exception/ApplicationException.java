package com.example.natterapi.exception;

import lombok.Getter;

/**
 * Application exception that holds the necessary information from the exception. This information
 * is used for populating the SOAPFault Detail element.
 *
 * @author daniel.morales
 */
@Getter
public class ApplicationException extends RuntimeException {

    /*** Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /*** Exception code and details used to propagate it up the chain. */
    private ExceptionCode exceptionCode;

    /**
     * Optional arguments.
     */
    private String[] args;

    /**
     * ApplicationException constructor.
     *
     * @param exceptionCode exception code
     * @param e             exception cause
     */
    public ApplicationException(ExceptionCode exceptionCode, Throwable e) {
        super(e);
        this.exceptionCode = exceptionCode;
    }

    /**
     * ApplicationException constructor.
     *
     * @param exceptionCode exception code
     * @param message       exception message
     */
    public ApplicationException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    /**
     * ApplicationException constructor.
     *
     * @param cause         exception cause
     * @param exceptionCode exception code
     * @param message       exception message
     */
    public ApplicationException(Throwable cause, ExceptionCode exceptionCode, String message) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }

    /**
     * ApplicationException constructor.
     *
     * @param exceptionCode exception code
     * @param message       exception message
     * @param args          arguments to be passes in predefined message
     */
    public ApplicationException(ExceptionCode exceptionCode, String message, String... args) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.args = args.clone();
    }

    /**
     * ApplicationException constructor.
     *
     * @param cause         the cause
     * @param exceptionCode exception code
     * @param message       exception message
     * @param args          arguments to be passes in predefined message
     */
    public ApplicationException(Throwable cause, ExceptionCode exceptionCode, String message, String... args) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
        this.args = args.clone();
    }

    /**
     * ApplicationException constructor.
     *
     * @param message exception message
     */
    public ApplicationException(String message) {
        super(message);
        this.exceptionCode = ExceptionCode.INTERNAL_ERROR;
    }

    /**
     * ApplicationException constructor.
     *
     * @param e exception cause
     */
    public ApplicationException(Throwable e) {
        super(e);
        this.exceptionCode = ExceptionCode.INTERNAL_ERROR;
    }
}
