package mx.gob.queretaro.services.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TdRecursoRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class RecursoRolServiceImplTest {

	@Autowired
	private IRecursoRolService recursoRolService;


	@Test
	public void obtenerRecursos_basic() throws InternalException {
		List<TdRecursoRol> recursos = recursoRolService.obtenerRecursos();

		assertThat(!recursos.isEmpty());
	}

}
