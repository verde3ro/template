package mx.gob.queretaro.services.service;

import org.springframework.data.domain.Page;

import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TcParametro;
import mx.gob.queretaro.services.request.ParametroRequest;

public interface IParametroService {
	TcParametro obtenerPorNombre(String nombre) throws InternalException;

	Page<TcParametro> obtenerPaginacion(int limit, int offset, String order, String sort, final String search);

	long guardarParametro(ParametroRequest parametroDto, String usuario) throws InternalException;

	long borrarParametro(long idParametro, String usuarioSesion) throws InternalException;
}
