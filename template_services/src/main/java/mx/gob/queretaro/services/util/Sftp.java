package mx.gob.queretaro.services.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.exception.InvalidOperationException;
import mx.gob.queretaro.repositories.repository.IParametroRepository;

@Slf4j
@Component
public class Sftp {

	private final IParametroRepository parametroRepository;
	private String servidor;
	private Integer puerto;
	private String usuario;
	private String contrasena;
	private String ruta;
	private SFTPConnectionPool connectionPool;

	@Autowired
	public Sftp(IParametroRepository parametroRepository){
		this.parametroRepository = parametroRepository;
	}

	@PostConstruct
	public void init() {
		/*try {
			this.servidor = parametroRepository.obtenerPorNombre("FTP_SERVIDOR", "AC").getValor().trim();
			this.puerto = Integer.parseInt(parametroRepository.obtenerPorNombre("FTP_PUERTO", "AC").getValor());
			this.usuario = parametroRepository.obtenerPorNombre("FTP_USUARIO", "AC").getValor();
			this.contrasena = Aes.desencriptar(parametroRepository.obtenerPorNombre("FTP_CONTRASENA", "AC").getValor(), null);
			this.ruta = parametroRepository.obtenerPorNombre("FTP_RUTA", "AC").getValor();
			connectionPool = new SFTPConnectionPool(10000000);
		} catch (InternalException ex) {
			log.error(ex.getMessage(), Sftp.class);
		}*/
	}

	public ChannelSftp conectar() throws InternalException {
		ChannelSftp channelSftp = null;

		try {
			String key = null;
			channelSftp = connectionPool.connect(servidor, puerto, usuario, contrasena, key);
		} catch (Exception ex) {
			log.error("Ocurrió un error al conectarse al servidor FTP.", ex);
			this.desconectar(channelSftp);
			throw new InternalException("Ocurrió un error al guardar su solicitud estatus: (0096).");
		}

		return channelSftp;
	}

	public boolean crearDirectorio(ChannelSftp channelSftp, String nombre) throws InternalException {
		boolean resultado = false;

		try {
			String path = ruta + nombre;

			if (channelSftp != null && !fileExists(path.replaceAll("//", "/"), channelSftp)) {
				channelSftp.mkdir(path.replaceAll("//", "/"));
			}
		} catch (Exception ex) {
			log.error(String.format("Ocurrió un error al crear el directorio %s en el servidor FTP.", nombre), ex);
			this.desconectar(channelSftp);
			throw new InternalException("Ocurrió un error al guardar su solicitud estatus: (0097).");
		}

		return resultado;
	}

	public boolean elimarDirectorio(ChannelSftp channelSftp, String ubicacion) throws InternalException {
		boolean resultado = false;

		try {
			String path = String.format("%s/%s", ruta, ubicacion);
			if (channelSftp != null && fileExists(path.replaceAll("//", "/"), channelSftp)) {
				channelSftp.rmdir(path.replaceAll("//", "/"));
				resultado = true;
			}
		} catch (Exception ex) {
			log.error("Ocurrió un error al eliminar el directorio " + ubicacion + " en el servidor FTP.", ex);
			this.desconectar(channelSftp);
			throw new InternalException("Ocurrió un error al guardar su solicitud estatus: (0090).");
		}

		return resultado;
	}

	public boolean cambiarDirectorio(ChannelSftp channelSftp, String ubicacion) throws InternalException {
		boolean resultado = false;

		try {
			String path = String.format("%s/%s", ruta, ubicacion);
			if (channelSftp != null) {

				channelSftp.cd(path.replaceAll("//", "/"));
				resultado = true;
			}
		} catch (Exception ex) {
			log.error("Ocurrió un error al cambiar de directorio en el servidor FTP.", ex);
			this.desconectar(channelSftp);
			throw new InternalException("Ocurrió un error al guardar su solicitud estatus: (0098).");
		}

		return resultado;
	}

	public boolean subirArchivo(ChannelSftp channelSftp, InputStream archivo, String ubicacion, String nombre) throws InternalException {
		boolean resultado = false;

		try {
			String path = String.format("%s/%s", ruta, ubicacion);

			if (channelSftp != null) {
				channelSftp.put(archivo, nombre.trim());

				if (!verificaExisteArchivo(channelSftp, path.replaceAll("//", "/") + "/" + nombre.trim())) {
					throw new InvalidOperationException("Ocurrió un error al guardar su solicitud estatus: (0099).");
				}

				resultado = true;
			}
		} catch (Exception ex) {
			log.error("Ocurrió un error al subir el archivo " + nombre + " en el servidor FTP.", ex);
			this.desconectar(channelSftp);
			throw new InternalException("Ocurrió un error al guardar su solicitud estatus: (0099).");
		}

		return resultado;
	}

	public boolean cambiaNombreArchivo(ChannelSftp channelSftp, String antiguo, String nuevo) throws InternalException {
		boolean resultado = false;

		try {
			String path = String.format("%s/", ruta);

			if (channelSftp != null) {
				channelSftp.rename(path.replaceAll("//", "/") + antiguo, path.replaceAll("//", "/") + nuevo);

				resultado = true;
			}
		} catch (Exception ex) {
			log.error("Ocurrió un error al cambiar nombre del archivo " + antiguo + " en el servidor FTP.", ex);
			this.desconectar(channelSftp);
			throw new InternalException("Ocurrió un error al cambiar nombre del archivo " + antiguo + " en el servidor FTP.");
		}

		return resultado;
	}

	public boolean eliminarArchivo(ChannelSftp channelSftp, String nombre) throws InternalException {
		boolean resultado = false;

		try {
			String path = String.format("%s/%s", ruta, nombre);

			if (channelSftp != null && fileExists(path.replaceAll("//", "/"), channelSftp)) {
				channelSftp.rm(path.replaceAll("//", "/"));
				resultado = true;
			}
		} catch (Exception ex) {
			log.error("Ocurrió un error al eliminar archivo " + nombre + " en el servidor FTP.", ex);
			this.desconectar(channelSftp);
			throw new InvalidOperationException("Ocurrió un error al guardar su solicitud estatus: (0091).");
		}

		return resultado;
	}

	public BufferedInputStream descargarArchivo(ChannelSftp channelSftp, String ubicacion) throws InternalException {
		BufferedInputStream archivo = null;

		try {
			String path = String.format("%s/%s", ruta, ubicacion);

			if (channelSftp != null) {
				archivo = new BufferedInputStream(channelSftp.get(path.replaceAll("//", "/")));
			}
		} catch (Exception ex) {
			log.error("Ocurrió un error al descargar el archivo " + ubicacion + " del servidor FTP.", ex);
			this.desconectar(channelSftp);
			throw new InternalException("Ocurrió un error al descargar el archivo " + ubicacion + " del servidor FTP.");
		}

		return archivo;
	}

	public boolean verificaExisteArchivo(ChannelSftp channelSftp, String path) throws InternalException {
		boolean flag = false;

		try {
			if (channelSftp != null) {
				flag = fileExists(path, channelSftp);
			}
		} catch (Exception ex) {
			log.error("Ocurrió un error al verificar el archivo " + path + " del servidor FTP.", ex);
			throw new InternalException("Ocurrió un error al verificar el archivo " + path + " del servidor FTP.");
		}

		return flag;
	}

	public boolean desconectar(ChannelSftp channelSftp) {
		boolean resultado = false;

		try {
			connectionPool.disconnect(channelSftp);
			resultado = true;
		} catch (IOException e) {
			resultado = false;
		}

		return resultado;
	}

	private boolean fileExists(String dir, ChannelSftp channel) {
		try {
			channel.stat(dir);
			return true;
		} catch (SftpException e) {
			return false;
		}
	}

}
