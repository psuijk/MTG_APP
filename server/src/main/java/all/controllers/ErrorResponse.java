package all.controllers;

import all.domain.Result;
import all.domain.ResultType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
    // write two methods
    // simple response
    public static ResponseEntity<ErrorResponse> build(String message){
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ResponseEntity<Object> build(Result<T> result){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if(result.getType() == null || result.getType() == ResultType.INVALID){
            status = HttpStatus.BAD_REQUEST;
        }else if(result.getType() == ResultType.NOT_FOUND){
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(result.getMessages(), status);
    }
}
