package travs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private Integer statusCode;
    private String message;
}
