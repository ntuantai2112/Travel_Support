package travs.exception;

import travs.constant.StatusCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


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



    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
