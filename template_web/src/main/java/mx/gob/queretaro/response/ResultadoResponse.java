package mx.gob.queretaro.response;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author rverde
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ResultadoResponse implements Serializable {

	private static final long serialVersionUID = 1634163484790667744L;
	private String estatus;
	private String mensaje;
	private Object datos;

}
