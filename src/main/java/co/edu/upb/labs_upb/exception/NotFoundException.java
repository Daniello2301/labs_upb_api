package co.edu.upb.labs_upb.exception;


/**
 * Exception class for handling not found errors.
 * This class extends RestException and is used when the server can not find the requested resource.
 */
public class NotFoundException extends RestException { //404

    /**
     * Default constructor for NotFoundException.
     */
    public NotFoundException() {
        super();
    }

    /**
     * Constructor for NotFoundException that accepts an ErrorDto object.
     *
     * @param errorDto The ErrorDto object containing error details
     */
    public NotFoundException(ErrorDto errorDto) {
        super(errorDto);
    }
}
