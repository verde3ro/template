package mx.gob.queretaro.repositories.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import mx.gob.queretaro.repositories.model.TdRecursoRol;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class RecursoRolRepositoryTest {

	@Autowired
	private IRecursoRolRepository repository;

	@Test
	public void findAll_basic() {
		List<TdRecursoRol> recursoRol = repository.findAll(Sort.by("tcRecurso.id").ascending());

		assertThat(!recursoRol.isEmpty());
	}
}
