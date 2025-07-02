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

    public ApiError(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static ApiError from(int status,String message, List<FieldValidationError> errors) {
        return ApiError.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(status)
                .message(message)
                .result(errors)
                .build();
    }


}
