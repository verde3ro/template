package mx.gob.queretaro.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PaginacionResponse {

	private long total;
	private Object rows;

}
