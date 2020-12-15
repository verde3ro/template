package mx.gob.queretaro.services.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GeqWsRequest implements Serializable {

	private static final long serialVersionUID = -4399350647564862850L;
	@JsonProperty("sistema")
	private String sistema;

	@JsonProperty("subject")
	private String subject;

	@JsonProperty("mensaje")
	private String mensaje;

	@JsonProperty("to")
	private List<String> to;

	@JsonProperty("cc")
	private List<String> cc;

	@JsonProperty("cco")
	private List<String> cco;

	@JsonProperty("archivos")
	private List<Map<String,String>> archivos;
	

}
