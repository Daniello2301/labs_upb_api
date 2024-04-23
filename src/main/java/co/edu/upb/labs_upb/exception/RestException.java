package co.edu.upb.labs_upb.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;


/**
 * Exception class for handling REST exceptions.
 * This class extends Exception and is used when a REST operation encounters an error.
 */
@Setter
@Getter
public class RestException extends Exception {

    /**
     * Serial version UID for serialization and deserialization.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ErrorDto object associated with the exception.
     */
    private ErrorDto errorDto;

    /**
     * Default constructor for RestException.
     */
    public RestException() {
        super();
    }

    /**
     * Constructor for RestException that accepts an ErrorDto object.
     *
     * @param errorDto The ErrorDto object containing error details
     */
    public RestException(ErrorDto errorDto) {
        super(errorDto.getError());
        this.errorDto = errorDto;
    }


    /**
     * Constructor for RestException that accepts a message string.
     *
     * @param msg The error message
     */
    public RestException(String msg) {
        super(msg);
    }

    /**
     * Constructor for RestException that accepts a message string and an Exception object.
     *
     * @param msg The error message
     * @param ex The exception
     */
    public RestException(String msg, Exception ex) {
        super(msg, ex);
    }

}
