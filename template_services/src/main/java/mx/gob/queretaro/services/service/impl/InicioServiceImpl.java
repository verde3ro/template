package mx.gob.queretaro.services.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.repository.IParametroRepository;
import mx.gob.queretaro.services.service.IInicioService;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class InicioServiceImpl implements IInicioService {

	private final IParametroRepository parametroRepository;

	@Autowired
	public InicioServiceImpl(IParametroRepository parametroRepository) {
		this.parametroRepository = parametroRepository;
	}

	@Override
	public String obtieneAvisoPrivacidad() throws InternalException {

		try {
			return parametroRepository.obtenerPorNombre("AVISO_PRIVACIDAD", "AC").getValor();
		} catch (Exception ex) {
			log.error("Ocurrió un error al obtener el aviso de privacidad.", ex);
			throw new InternalException("Ocurrió un error al obtener el aviso de privacidad.", ex, InicioServiceImpl.class);
		}

	}

}
