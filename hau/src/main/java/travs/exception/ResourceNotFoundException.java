package travs.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends GeneralException {

    public ResourceNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }
}
