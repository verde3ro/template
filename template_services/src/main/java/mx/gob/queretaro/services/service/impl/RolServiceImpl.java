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
import mx.gob.queretaro.repositories.model.TcRol;
import mx.gob.queretaro.repositories.model.TdRecursoRol;
import mx.gob.queretaro.repositories.model.TdRecursoRolPK;
import mx.gob.queretaro.repositories.repository.IRecursoRolRepository;
import mx.gob.queretaro.repositories.repository.IRolRepository;
import mx.gob.queretaro.services.request.RecursoRequest;
import mx.gob.queretaro.services.request.RolRequest;
import mx.gob.queretaro.services.service.IRolService;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RolServiceImpl implements IRolService {

	private final IRolRepository rolRepository;
	private final IRecursoRolRepository recursoRolRepository;

	@Autowired
	public RolServiceImpl(IRolRepository rolRepository, IRecursoRolRepository recursoRolRepository) {
		this.rolRepository = rolRepository;
		this.recursoRolRepository = recursoRolRepository;
	}

	@Override
	public List<TcRol> obtenerRolesPorRol(String rol)throws InternalException{
		if (rol != null && !rol.trim().isEmpty()) {
			try {
				return rolRepository.obtenerRolesPorRol(rol, "AC");
			} catch (Exception ex) {
				log.error("Ocurrió un error.", ex);
				throw new InternalException("Ocurrió un error .");
			}
		} else {
			throw new InternalException("El rol no debe ser nulo o vacío .");
		}
	}

	@Override
	public Page<TcRol> obtenerRoles(int limit, int offset, String order, String sort, String search) {
		return rolRepository.obtenerRoles(search, PageRequest.of((offset / limit), limit,  Sort.by(new Sort.Order((order.equals("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC, sort))));
	}

	@Override
	public List<TcRecurso> obtenerRecursosPorIdRol(long idRol)throws InternalException {
		if (idRol > 0) {
			try {
				return rolRepository.obtenerRecursosPorIdRol(idRol, "AC");
			} catch (Exception ex) {
				log.error("Ocurrió un error al obtener los recursos del rol. ", ex);
				throw new InternalException("Ocurrió un error al obtener los recursos del rol.");
			}
		} else {
			throw new InternalException("El rol no debe ser nulo o vacío .");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TcRol guardarRol(RolRequest rolRequest, String usuarioSesion)throws InternalException {
		if (rolRequest != null && !rolRequest.getRecursos().isEmpty() && usuarioSesion != null) {
			try {
				TcRol rol;

				if (rolRequest.getId() == 0) {
					rol = rolRepository.obtenerRolPorRol(rolRequest.getRol().trim().toUpperCase(), "IN");

					if (rol == null) {
						rol = new TcRol();

						rol.setRol(rolRequest.getRol().trim().toUpperCase());
						rol.setDescripcion(rolRequest.getDescripcion().trim());
						rol.setEstatus("AC");
						rol.setUsuarioI(usuarioSesion.trim());
						rol.setFechaI(new Date());

						rol = rolRepository.save(rol);
					} else {
						rol.setRol(rolRequest.getRol().trim().toUpperCase());
						rol.setDescripcion(rolRequest.getDescripcion().trim());
						rol.setEstatus("AC");
						rol.setUsuarioU(usuarioSesion.trim());
						rol.setFechaU(new Date());

						rolRepository.deleteById(rol);
					}
				} else {
					rol = rolRepository.obtenerRolPorRol(rolRequest.getRol().trim().toUpperCase(), "AC");

					rol.setRol(rolRequest.getRol().trim().toUpperCase());
					rol.setDescripcion(rolRequest.getDescripcion().trim());
					rol.setEstatus("AC");
					rol.setUsuarioU(usuarioSesion.trim());
					rol.setFechaU(new Date());

					rolRepository.borrarRecursoRol(rol.getId());
					rolRepository.save(rol);
				}

				guardaRecursoRol(rolRequest, usuarioSesion, rol);

				return rol;
			} catch (Exception ex) {
				log.error("Ocurrió un error al guardar el rol.", ex);
				throw new InternalException("Ocurrió un error al guardar el rol.");
			}
		} else {
			throw new InternalException("El rol no debe ser nulo o vacío.");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public long borrarRol(long idRol, String usuarioSesion)throws InternalException {
		if (idRol > 0 && usuarioSesion != null) {
			try {
				rolRepository.borrarRol(idRol);

				rolRepository.borrarUsuarioRol(idRol);

				rolRepository.borrarRecursoRol(idRol);

				return idRol;
			} catch (Exception ex) {
				log.error("Ocurrió un error al borrar el rol.", ex);
				throw new InternalException("Ocurrió un error al borrar el rol.");
			}
		} else {
			throw new InternalException("El rol no debe ser nulo o vacío.");
		}
	}

	@Override
	public List<TcRol> obtenerRoles() throws InternalException {
		try {
			return rolRepository.obtenerRoles("AC");
		} catch (Exception ex) {
			log.error("Ocurrió un error al obtener los roles.", ex);
			throw new InternalException("Ocurrió un error al obtener los roles.");
		}
	}

	private void guardaRecursoRol(RolRequest rolRequest, String usuarioSesion, TcRol rol) {
		TdRecursoRol recursoRol;

		for (RecursoRequest recursoRequest : rolRequest.getRecursos()) {
			recursoRol = new TdRecursoRol();

			recursoRol.setId(new TdRecursoRolPK(recursoRequest.getId(), rol.getId()));
			recursoRol.setUsuarioI(usuarioSesion.trim());
			recursoRol.setFechaI(new Date());
			recursoRol.setEstatus("AC");

			recursoRolRepository.save(recursoRol);
		}
	}
}
