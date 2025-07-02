package travs.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldValidationError {

    private String field;
    private Object rejectedValue;
    private String message;
}
