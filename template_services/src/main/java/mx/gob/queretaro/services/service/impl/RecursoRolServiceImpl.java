package mx.gob.queretaro.services.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TdRecursoRol;
import mx.gob.queretaro.repositories.repository.IRecursoRolRepository;
import mx.gob.queretaro.services.service.IRecursoRolService;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RecursoRolServiceImpl implements IRecursoRolService {

	private final IRecursoRolRepository recursoRolRepository;

	@Autowired
	public RecursoRolServiceImpl(IRecursoRolRepository recursoRolRepository) {
		this.recursoRolRepository = recursoRolRepository;
	}

	@Override
	public List<TdRecursoRol> obtenerRecursos() throws InternalException {
		try {
			return recursoRolRepository.findAll(Sort.by("TcRecurso.id").ascending());
		} catch (Exception ex) {
			log.error("Ocurrió un error al obtener los recursos de la aplicación.", ex);
			throw new InternalException("Ocurrió un error al obtener los recursos de la aplicación.", ex, RecursoRolServiceImpl.class);
		}
	}

}
