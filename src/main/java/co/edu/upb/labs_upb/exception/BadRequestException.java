package co.edu.upb.labs_upb.exception;

/**
 * Exception class for handling bad requests.
 * This class extends RestException and is used when a client sends a request that the server cannot or will not process.
 */
public class BadRequestException extends RestException { //4xx

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for BadRequestException.
     */
    public BadRequestException() {
        super();
    }

    /**
     * Constructor for BadRequestException that accepts an ErrorDto object.
     *
     * @param errorDto The ErrorDto object containing error details
     */
    public BadRequestException(ErrorDto errorDto) {
        super(errorDto);
    }


    /**
     * Constructor for BadRequestException that accepts a message string.
     *
     * @param msg The error message
     */
    public BadRequestException(String msg) {
        super(msg);
    }
}
