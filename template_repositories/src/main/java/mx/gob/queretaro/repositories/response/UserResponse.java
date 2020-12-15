package mx.gob.queretaro.repositories.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String account;
	private String firstName;
	private String lastName;
	private String email;
	private String expire;
	private boolean enabled;

}
