package mx.gob.queretaro.repositories.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RolResponse implements Serializable {

	private static final long serialVersionUID = 8377652345211339045L;

	@JsonProperty("id")
	private long id;

	@JsonProperty("descripcion")
	private String descripcion;

	@JsonProperty("nombre")
	private String nombre;

}
