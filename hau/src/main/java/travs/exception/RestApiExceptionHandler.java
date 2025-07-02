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

    /**
     * Handles RestApiException by returning a structured error response with HTTP 400 status.
     *
     * @param restApiException the exception containing API error details
     * @return a ResponseEntity with an ApiError body and HTTP status 400 (Bad Request)
     */
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<?> handlerException(RestApiException restApiException) {
        ApiError apiError = new ApiError(restApiException.getStatus(), restApiException.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles validation exceptions for method arguments and returns a structured error response.
     *
     * Extracts field-specific validation errors from the exception, constructs an {@link ApiError} containing details about each invalid field, and responds with HTTP 400 (Bad Request).
     *
     * @param ex the exception containing validation errors for method arguments
     * @return a {@link ResponseEntity} with an {@link ApiError} describing the validation failures and HTTP 400 status
     */
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

    /**
     * Handles authentication exceptions by returning a structured error response for login failures.
     *
     * Returns an HTTP 422 (Unprocessable Entity) response with a fixed message indicating invalid username or password.
     *
     * @return a ResponseEntity containing the ApiError and HTTP status 422
     */
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
