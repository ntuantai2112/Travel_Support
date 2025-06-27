package travs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseDTO<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long recordsTotal;
	private Long recordsFiltered;
	private List<T> data;
}
