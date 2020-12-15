package mx.gob.queretaro.services.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TcRol;
import mx.gob.queretaro.repositories.model.TcUsuario;
import mx.gob.queretaro.services.request.UsuarioRequest;

public interface IUsuarioService {

	TcUsuario obtenerUsuario(String usuario) throws InternalException;

	Page<TcUsuario> obtenerUsuarios(int limit, int offset, String order, String sort, String search) throws InternalException;

	List<TcRol> obtenerRolesPorIdUsuario(long idUsuario) throws InternalException;

	TcUsuario guardarUsuario(UsuarioRequest usuarioRequest, String usuarioSesion) throws InternalException;

	long borrarUsuario(long idUsuario, String usuarioSesion) throws InternalException;

	Map<String, Object> obtenerUsuarioLdap(String usuario) throws InternalException;

}
