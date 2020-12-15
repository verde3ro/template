package mx.gob.queretaro.services.respose;


import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GeqWsResponse implements Serializable {

	private static final long serialVersionUID = 3494232010900031695L;

	@JsonProperty("estatus")
	private String estatus;

	@JsonProperty("mensaje")
	private String mensaje;

	@JsonProperty("datos")
	private Map<String, Object> datos;
}
