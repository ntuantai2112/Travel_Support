package travs.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class RestApiExceptionHandler {

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<?> handlerException(RestApiException restApiException) {
        ApiError apiError = new ApiError(restApiException.getStatus(), restApiException.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldValidationError> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldValidationError(
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()
                ))
                .toList();

        ApiError apiError = ApiError.from(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                validationErrors
        );
        log.warn("Validation error: {} field(s) failed. Details: {}", validationErrors.size(), validationErrors);
        log.debug("Exception stacktrace:", ex);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Lỗi Login
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthException(AuthenticationException ex) {

        ApiError error = ApiError.builder()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message("Invalid username/password")
                .build();
        log.warn("Login failed: {}", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }




}
