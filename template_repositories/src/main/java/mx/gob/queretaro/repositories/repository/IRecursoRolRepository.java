package mx.gob.queretaro.repositories.repository;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import mx.gob.queretaro.repositories.model.TdRecursoRol;


public interface IRecursoRolRepository extends JpaRepository<TdRecursoRol, Serializable> {

}
