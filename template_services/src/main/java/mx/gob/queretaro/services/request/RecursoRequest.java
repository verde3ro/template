package mx.gob.queretaro.services.request;

import java.io.Serializable;

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
public class RecursoRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	@NotNull
	@Size(min = 1, max = 250)
	private String recurso;
	@NotNull
	@Size(min = 1, max = 250)
	private String descripcion;
	@NotNull
	private Long sesion;

}
