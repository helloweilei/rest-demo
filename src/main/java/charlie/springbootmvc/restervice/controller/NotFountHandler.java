package charlie.springbootmvc.restervice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFountHandler {

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity handleNotFound() {
//        return new ResponseEntity(HttpStatus.NOT_FOUND);
//    }
}
