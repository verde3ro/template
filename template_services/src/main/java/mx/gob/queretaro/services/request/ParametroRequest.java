package mx.gob.queretaro.services.request;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class ParametroRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal id;
	@NotNull
	@Size(min = 1, max = 50)
	private String nombre;
	@NotNull
	@Size(min = 1, max = 4000)
	private String valor;

}
