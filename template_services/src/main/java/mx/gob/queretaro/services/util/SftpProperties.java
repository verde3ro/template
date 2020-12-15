package mx.gob.queretaro.services.util;

import com.jcraft.jsch.ChannelSftp;
import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@Data
@ConfigurationProperties(prefix = "sftp")
public class SftpProperties {

	private String servidor;
	private Integer puerto;
	private String usuario;
	private String contrasena;
	private Pool pool = new Pool();

	@PostConstruct
	public void init() {
		/*
		servidor = parametroRepository.obtenerPorNombre("FTPSERVIDOR", "AC").getValor().trim();
		puerto = Integer.parseInt(parametroRepository.obtenerPorNombre("FTPPUERTO", "AC").getValor());
		usuario = parametroRepository.obtenerPorNombre("FTPUSUARIO", "AC").getValor();
		contrasena = parametroRepository.obtenerPorNombre("FTPCONTRASENA", "AC").getValor();
		ruta = parametroRepository.obtenerPorNombre("FTPRUTA", "AC").getValor();
		 */
	}

	public static class Pool extends GenericObjectPoolConfig<ChannelSftp> {
		@Value("${sftp.maxtotal}")
		private int maxTotal;
		@Value("${sftp.maxidle}")
		private int maxIdle;
		@Value("${sftp.maxidle}")
		private int minIdle;

		public Pool() {
			super();
		}

		@Override
		public int getMaxTotal() {
			return maxTotal;
		}

		@Override
		public void setMaxTotal(int maxTotal) {
			this.maxTotal = maxTotal;
		}

		@Override
		public int getMaxIdle() {
			return maxIdle;
		}

		@Override
		public void setMaxIdle(int maxIdle) {
			this.maxIdle = maxIdle;
		}

		@Override
		public int getMinIdle() {
			return minIdle;
		}

		@Override
		public void setMinIdle(int minIdle) {
			this.minIdle = minIdle;
		}

	}

}
