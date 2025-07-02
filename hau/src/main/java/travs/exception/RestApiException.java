package travs.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import travs.constant.StatusCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@AllArgsConstructor
public class RestApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer status;

    private String message;

    public RestApiException() {
    }

    public RestApiException(StatusCode statusCode) {
        this.status = statusCode.getStatus();
        this.message = statusCode.getMessage();
    }



}
