package travs.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError<T> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp = LocalDateTime.now();
    private Integer statusCode;
    private String message;
    private T result;

    /**
     * Constructs an ApiError with the specified HTTP status code and error message.
     *
     * @param statusCode the HTTP status code representing the error
     * @param message a descriptive message explaining the error
     */
    public ApiError(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    /**
     * Creates an {@code ApiError} instance with the specified HTTP status code, error message, and a list of field validation errors as the result.
     *
     * @param status the HTTP status code representing the error
     * @param message a descriptive error message
     * @param errors a list of field validation errors to include in the result
     * @return an {@code ApiError} populated with the provided status, message, and errors, and the current timestamp
     */
    public static ApiError from(int status,String message, List<FieldValidationError> errors) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(status)
                .message(message)
                .result(errors)
                .build();
    }


}
