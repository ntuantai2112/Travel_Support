package travs.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class SQLQueryParam {
    private String sql;
    private Map<String, Object> params;
}
