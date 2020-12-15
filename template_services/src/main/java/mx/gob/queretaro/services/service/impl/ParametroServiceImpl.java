package mx.gob.queretaro.services.service.impl;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.exception.InvalidOperationException;
import mx.gob.queretaro.repositories.model.TcParametro;
import mx.gob.queretaro.repositories.repository.IParametroRepository;
import mx.gob.queretaro.services.request.ParametroRequest;
import mx.gob.queretaro.services.service.IParametroService;


@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ParametroServiceImpl implements IParametroService {

	private final IParametroRepository parametroRepository;

	@Autowired
	public ParametroServiceImpl(IParametroRepository parametroRepository) {
		this.parametroRepository = parametroRepository;
	}

	@Override
	public TcParametro obtenerPorNombre(String nombre) throws InternalException{
		if (nombre != null && !nombre.trim().isEmpty()) {
			try {
				return parametroRepository.obtenerPorNombre(nombre.trim(), "AC");
			} catch (Exception ex) {
				log.error("Ocurrió un error al obtener el parámetro.", ex);
				throw new InternalException("Ocurrió un error al obtener el parámetro.", ex, ParametroServiceImpl.class);
			}
		} else {
			throw new InvalidOperationException("El nombre del parámetro no debe ser nulo o vacío.");
		}
	}

	@Override
	public Page<TcParametro> obtenerPaginacion(int limit, int offset, String order, String sort, String search) {
		return parametroRepository.obtenerParametros(search, PageRequest.of((offset / limit), limit,  Sort.by(new Sort.Order((order.equals("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC, sort))));
	}

	@Override
	public long guardarParametro(ParametroRequest parametroRequest, String usuario) throws InternalException{
		if (parametroRequest != null && usuario != null) {
			try {
				TcParametro parametro;

				if (parametroRequest.getId() == null) {
					parametro = parametroRepository.obtenerPorNombre(parametroRequest.getNombre().trim(), "IN");

					if (parametro == null) {
						parametro = new TcParametro();

						parametro.setNombre(parametroRequest.getNombre().trim());
						parametro.setValor(parametroRequest.getValor().trim());
						parametro.setEstatus("AC");
						parametro.setUsuarioI(usuario.trim());
						parametro.setFechaI(new Date());
					} else {
						actualizarPrametro(parametroRequest, usuario, parametro);
					}
				} else {
					parametro = parametroRepository.obtenerPorNombre(parametroRequest.getNombre().trim(), "AC");
					actualizarPrametro(parametroRequest, usuario, parametro);
				}

				parametro = parametroRepository.save(parametro);
				return parametro.getId();
			} catch (Exception ex) {
				log.error("Ocurrió un error al guardar el parámetro.", ex);
				throw new InternalException("Ocurrió un error al guardar el parámetro.", ex, ParametroServiceImpl.class);
			}
		} else {
			throw new InternalException("El parámetro no debe ser nulo o vacío.", ParametroServiceImpl.class);

		}
	}

	@Override
	public long borrarParametro(long idParametro, String usuario) throws InternalException{
		if (idParametro > 0 && usuario != null) {
			try {
				parametroRepository.borrarParametro(idParametro, usuario, new Date());

				return idParametro;
			} catch (Exception ex) {
				log.error("Ocurrió un error al borrar el parámetro.", ex);
				throw new InternalException("Ocurrió un error al borrar el parámetro.", ex, ParametroServiceImpl.class);

			}
		} else {
			throw new InternalException("El parámetro no debe ser nulo o vacío.", ParametroServiceImpl.class);
		}
	}

	private void actualizarPrametro(ParametroRequest parametroRequest, String usuario, TcParametro parametro) {
		parametro.setNombre(parametroRequest.getNombre().trim());
		parametro.setValor(parametroRequest.getValor().trim());
		parametro.setEstatus("AC");
		parametro.setUsuarioU(usuario.trim());
		parametro.setFechaU(new Date());
	}

}