package mx.gob.queretaro;

import mx.gob.queretaro.services.util.SftpFactory;
import mx.gob.queretaro.services.util.SftpHelper;
import mx.gob.queretaro.services.util.SftpPool;
import mx.gob.queretaro.services.util.SftpProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SftpProperties.class)
public class SftpConfiguration {

	// FactorySfp
	@Bean
	public SftpFactory sftpFactory(SftpProperties properties) {
		return new SftpFactory(properties);
	}

	// Pool de conexiones
	@Bean
	public SftpPool sftpPool(SftpFactory sftpFactory) {
		return new SftpPool(sftpFactory);
	}

	// Clase auxiliar
	@Bean
	public SftpHelper sftpHelper(SftpPool sftpPool) {
		return new SftpHelper(sftpPool);
	}
}
