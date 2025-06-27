package travs.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApiResponse {
    private Long totalElement;
    private Object data;
}
