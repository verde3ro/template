package mx.gob.queretaro.services.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TcRecurso;
import mx.gob.queretaro.repositories.repository.IRecursoRepository;
import mx.gob.queretaro.services.request.RecursoRequest;
import mx.gob.queretaro.services.service.IRecursoService;

/**
 *
 * @author llunah
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RecursoServiceImpl implements IRecursoService {

	private final IRecursoRepository recursoRepository;

	@Autowired
	public RecursoServiceImpl(IRecursoRepository recursoRepository) {
		this.recursoRepository = recursoRepository;
	}

	@Override
	public List<TcRecurso> obtenerRecursosPorRecurso(String recurso) throws InternalException{
		if (recurso != null && !recurso.trim().isEmpty()) {
			try {
				return recursoRepository.obtenerRecursosPorRecurso(recurso.trim().toUpperCase(), "AC");
			} catch (Exception ex) {
				log.error("Ocurrió un error al obtener los campos.", ex);
				throw new InternalException("Ocurrió un error al obtener los campos.", ex, RecursoServiceImpl.class);
			}
		} else {
			throw new InternalException("El recurso no debe ser nulo o vacío. ", RecursoServiceImpl.class);
		}
	}

	@Override
	public Page<TcRecurso> obtenerPaginacion(int limit, int offset, String order, String sort, String search){
		return recursoRepository.obtenerRecursos(search, PageRequest.of((offset / limit), limit, Sort.by(new Sort.Order((order.equals("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC, sort))));
	}

	@Override
	public List<TcRecurso> obtenerRecursosSinSesion() throws InternalException {
		try {
			return recursoRepository.obtenerRecursosSinSesion("AC");
		} catch (Exception ex) {
			log.error("Ocurrió un error al obtener los recusos sin sesión.", ex);
			throw new InternalException("Ocurrió un error al obtener  los recusos sin sesión.");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TcRecurso guardarRecurso(RecursoRequest recursoRequest, String usuarioSesion) throws InternalException{
		if (recursoRequest != null && usuarioSesion != null) {

			try {
				TcRecurso recurso;

				if (recursoRequest.getId() == 0) {
					recurso = recursoRepository.obtenerRecursoPorRecurso(recursoRequest.getRecurso().trim(), "IN");

					if (recurso == null) {
						recurso = new TcRecurso();

						recurso.setRecurso(recursoRequest.getRecurso().trim());
						recurso.setDescripcion(recursoRequest.getDescripcion().trim());
						recurso.setEstatus("AC");
						recurso.setUsuarioI(usuarioSesion.trim());
						recurso.setFechaI(new Date());
						recurso.setSesion(recursoRequest.getSesion());

						recurso = recursoRepository.save(recurso);
					} else {
						actualizaRecurso(recursoRequest, usuarioSesion, recurso);
					}
				} else {
					recurso = recursoRepository.obtenerRecursoPorId(recursoRequest.getId(), "AC");

					actualizaRecurso(recursoRequest, usuarioSesion, recurso);
				}

				return recurso;
			} catch (Exception ex) {
				log.error("Ocurrió un error al guardar el recurso.", ex);
				throw new InternalException("Ocurrió un error al guardar el recurso.", ex, RecursoServiceImpl.class);
			}
		} else {
			throw new InternalException("El recurso no debe ser nulo o vacío.", RecursoServiceImpl.class);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public long  borrarRecurso(long idRecurso, String usuarioSesion)throws InternalException {
		if (idRecurso > 0 && usuarioSesion != null) {
			try {
				recursoRepository.borrarRecurso(idRecurso);

				recursoRepository.borrarRecursoRol(idRecurso);

				return idRecurso;
			} catch (Exception ex) {
				log.error("Ocurrió un error al borrar el recurso.", ex);
				throw new InternalException("Ocurrió un error al borrar el recurso.", ex, RecursoServiceImpl.class);
			}
		} else {
			throw new InternalException("El recurso no debe ser nulo o vacío.", RecursoServiceImpl.class);
		}
	}

	private void actualizaRecurso(RecursoRequest recursoRequest, String usuarioSesion, TcRecurso recurso) {
		recurso.setRecurso(recursoRequest.getRecurso().trim());
		recurso.setDescripcion(recursoRequest.getDescripcion().trim());
		recurso.setEstatus("AC");
		recurso.setUsuarioU(usuarioSesion.trim());
		recurso.setFechaU(new Date());
		recurso.setSesion(recursoRequest.getSesion());

		recursoRepository.save(recurso);
	}

}
