package co.edu.upb.labs_upb.exception;

public class BadRequestException extends RestException{//4xx
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(ErrorDto errorDto) {
        super(errorDto);
    }

    public BadRequestException(String msg) {
        super(msg);
    }
}
