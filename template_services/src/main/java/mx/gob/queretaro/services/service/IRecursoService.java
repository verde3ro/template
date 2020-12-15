package mx.gob.queretaro.services.service;

import java.util.List;

import org.springframework.data.domain.Page;

import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.model.TcRecurso;
import mx.gob.queretaro.services.request.RecursoRequest;

public interface IRecursoService {

	List<TcRecurso> obtenerRecursosPorRecurso(String recurso)throws InternalException;

	Page<TcRecurso> obtenerPaginacion(int limit, int offset, String order, String sort, final String search);

	List<TcRecurso> obtenerRecursosSinSesion() throws InternalException;

	TcRecurso guardarRecurso(RecursoRequest recursoDto, String usuarioSesion)throws InternalException;

	long borrarRecurso(long idRecurso, String usuarioSesion)throws InternalException;
}
