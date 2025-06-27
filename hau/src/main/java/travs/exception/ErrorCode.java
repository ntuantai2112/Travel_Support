package travs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN("UNKNOWN", "unknown error"),
    UNAUTHORIZED("UNAUTHORIZED", "unauthorized"),
    INVALID_PARAMETER("INVALID_PARAMETER", "invalid parameter"),
    MISSING_PARAMETER("MISSING_PARAMETER", "missing parameter"),
    CONFLICT("CONFLICT", "conflict error"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "resource not found"),
    FAVORITE_ALREADY_EXIST("FAVORITE_ALREADY_EXIST", "favorite not found"),
    ;

    private final String code;
    private final String message;
}
