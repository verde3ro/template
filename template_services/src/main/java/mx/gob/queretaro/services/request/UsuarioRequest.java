package mx.gob.queretaro.services.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequest implements Serializable {

	private static final long serialVersionUID = -2995818271523119580L;

	private long id;

	@NotNull
	@Size(min = 1, max = 20)
	private String usuario;

	@NotNull
	@Size(min = 1, max = 250)
	private String nombre;

	@NotNull
	@Size(max = 100)
	private String correo;

	@NotNull
	private List<RolRequest> roles;

}
