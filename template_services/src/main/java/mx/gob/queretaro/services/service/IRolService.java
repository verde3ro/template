package mx.gob.queretaro.services.service;

import java.util.List;

import org.springframework.data.domain.Page;

import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TcRecurso;
import mx.gob.queretaro.repositories.model.TcRol;
import mx.gob.queretaro.services.request.RolRequest;

public interface IRolService {

	List<TcRol> obtenerRolesPorRol(String rol) throws InternalException;

	Page<TcRol> obtenerRoles(int limit, int offset, String order, String sort, String search);

	List<TcRecurso> obtenerRecursosPorIdRol(long idRol)throws InternalException;

	TcRol guardarRol(RolRequest usuarioDto, String usuarioSesion)throws InternalException;

	long borrarRol(long idRol, String usuarioSesion)throws InternalException;

	List<TcRol> obtenerRoles() throws InternalException;

}
