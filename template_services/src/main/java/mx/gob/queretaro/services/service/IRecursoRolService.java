package mx.gob.queretaro.services.service;

import java.util.List;

import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TdRecursoRol;

public interface IRecursoRolService {

	List<TdRecursoRol> obtenerRecursos() throws InternalException;
}
