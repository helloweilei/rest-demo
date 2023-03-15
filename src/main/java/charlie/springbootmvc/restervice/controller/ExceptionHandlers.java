package charlie.springbootmvc.restervice.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFound() {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> handleInvalidArguments(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("message", err.getDefaultMessage());
                    return errorMap;
                }).toList();
        return ResponseEntity.badRequest()
                .body(errors);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<?> handleTransactionSystemException(TransactionSystemException transactionSystemException) {
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.badRequest();
        if (transactionSystemException.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) transactionSystemException.getCause().getCause();
            var errors = cve.getConstraintViolations().stream()
                    .map(cv -> {
                        Map<String, String> errMap = new HashMap<>();
                        errMap.put(cv.getPropertyPath().toString(), cv.getMessage());
                        return errMap;
                    }).toList();
            return bodyBuilder.body(errors);

        }
        return bodyBuilder.build();

    }
}
