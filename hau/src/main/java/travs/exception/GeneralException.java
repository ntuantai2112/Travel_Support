package travs.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class GeneralException extends RuntimeException {

    private final ErrorCode errorCode;
    private Object[] messageData;
    private Map<String, Double> data;

    public GeneralException(ErrorCode errorCode, Object... messageData) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.messageData = messageData;
    }

    public GeneralException(ErrorCode errorCode, Map<String, Double> data, Object... messageData) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.data = data;
        this.messageData = messageData;
    }

    public GeneralException() {
        this(ErrorCode.UNKNOWN);
    }
}
