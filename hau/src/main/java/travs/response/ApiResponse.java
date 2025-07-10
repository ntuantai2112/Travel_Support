package travs.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private Long totalElement;
    private Object data;
    private Integer code;
    private String message;
    private T result;


    public static <T> ApiResponse<T> build(Integer code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> build(Integer code, String message, T result) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .result(result)
                .build();
    }

    public static <T> ApiResponse<T> build(Integer code, String message, T result, Long totalElement) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .result(result)
                .totalElement(totalElement)
                .build();
    }


}
