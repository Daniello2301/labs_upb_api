package co.edu.upb.labs_upb.exception;


import lombok.Getter;

import java.io.Serial;

/**
 * Exception class for handling internal server errors.
 * This class extends RestException and is used when the server encounters an unexpected condition that prevents it from fulfilling the request.
 */
@Getter
public class InternalServerErrorException extends RestException {

    /**
     * Serial version UID for serialization and deserialization.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Error code associated with the exception.
     */
    private String codigoError;

    /**
     * Constructor for InternalServerErrorException that accepts a message string, a code error string, and an Exception object.
     *
     * @param msg The error message
     * @param codigoError The error code
     * @param ex The exception
     */
    public InternalServerErrorException(String msg, String codigoError, Exception ex) {
        super(msg, ex);
        this.codigoError = codigoError;
    }

    /**
     * Constructor for InternalServerErrorException that accepts a message string and an Exception object.
     *
     * @param msg The error message
     * @param ex The exception
     */
    public InternalServerErrorException(String msg, Exception ex) {
        super(msg, ex);
    }

    /**
     * Default constructor for InternalServerErrorException.
     */
    public InternalServerErrorException() {
        super();
    }

    /**
     * Constructor for InternalServerErrorException that accepts an ErrorDto object.
     *
     * @param errorDto The ErrorDto object containing error details
     */
    public InternalServerErrorException(ErrorDto errorDto) {
        super(errorDto);
    }

}
