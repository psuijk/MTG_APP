package all.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleException(DataIntegrityViolationException ex) {
        return ErrorResponse.build("Something went wrong in the database. " +
                "Please ensure that any referenced records exist. Your request failed. :(");
    }

    /*@ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchIoException(Exception ex){
        return new ResponseEntity<>(new ErrorResponse("Sorry,that file doesn't exist :( "), HttpStatus.BAD_REQUEST);
    }*/

}
