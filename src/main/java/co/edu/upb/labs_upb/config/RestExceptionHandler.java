package co.edu.upb.labs_upb.config;

import co.edu.upb.labs_upb.exception.BadRequestException;
import co.edu.upb.labs_upb.exception.ErrorDto;
import co.edu.upb.labs_upb.exception.InternalServerErrorException;
import co.edu.upb.labs_upb.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;


/**
 * A class to handle exceptions in REST API.
 * It extends ResponseEntityExceptionHandler to provide centralized exception handling across all methods through @ControllerAdvice.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);


    /**
     * Handles general exceptions.
     *
     * @param e the exception
     * @return the response entity
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDto> getGeneralException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        ErrorDto errorRq = ErrorDto.getErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorRq, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles internal server exceptions.
     *
     * @param e the exception
     * @return the response entity
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({InternalServerErrorException.class})
    public ResponseEntity<ErrorDto> getGeneralException(InternalServerErrorException e) {
        LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getErrorDto(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles not found exceptions.
     *
     * @param e the exception
     * @return the response entity
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorDto> getNotFoundRequest(NotFoundException e) {
        LOGGER.info(e.getMessage());
        return new ResponseEntity<>(e.getErrorDto(), HttpStatus.NOT_FOUND);

    }


    /**
     * Handles bad request exceptions.
     *
     * @param e the exception
     * @return the response entity
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorDto> getBadRequestException(BadRequestException e) {
        LOGGER.info(e.getErrorDto().getMessage());
        return new ResponseEntity<>(e.getErrorDto(), HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles method argument not valid exceptions.
     *
     * @param ex the exception
     * @param headers the headers
     * @param status the status
     * @param request the request
     * @return the response entity
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                 HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();

        if (!fieldErrors.isEmpty()) {
            errorMessage.append(fieldErrors.get(0).getDefaultMessage());
        } else {
            errorMessage.append("Ocurrio un error al procesar la solicitud. Por favor verifique e intente de nuevo.");
        }

        ErrorDto errorInfo = ErrorDto.getErrorDto(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorMessage.toString(),
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
}
