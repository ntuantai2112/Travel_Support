package travs.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<D, M> {
    private boolean success;
    private D data;
    private M meta;
    private ErrorResponse error;

    public static <D> BaseResponse ok(D data) {
        return BaseResponse.builder()
                .data(data)
                .success(true)
                .build();
    }

    public static <D, M> BaseResponse<D, M>  ok(D data, M meta) {
        return BaseResponse.<D, M> builder()
                .data(data)
                .meta(meta)
                .success(true)
                .build();
    }

    public static BaseResponse ok() {
        return ok(null);
    }

    public static <D> BaseResponse error(D data, ErrorResponse error) {
        return BaseResponse.builder()
                .data(data)
                .error(error)
                .success(false)
                .build();
    }

    public static BaseResponse error(ErrorResponse error) {
        return error(null, error);
    }

    public static <D> BaseResponse error(D data) {
        return error(data, null);
    }

    public static BaseResponse error() {
        return error(null, null);
    }
}
