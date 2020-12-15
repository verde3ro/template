package mx.gob.queretaro.services.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.repositories.exception.InternalException;
import org.apache.commons.io.IOUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class SftpHelper {

	private final SftpPool pool;
	private String ruta;
	private static final String PATH = "%s/%s";

	public SftpHelper(SftpPool pool) {
		this.pool = pool;
	}

	@PostConstruct
	public void init() {
		// this.ruta = parametroRepository.obtenerPorNombre("FTPRUTA", "AC").getValor().trim().replaceAll("//", "/");
	}


	/**
	 * Crea directorio
	 * @param directorio directorio remoto
	 */
	public void crearDirectorio(String directorio) throws InternalException {
		ChannelSftp sftp = pool.borrowObject();

		try {
			String path = String.format(PATH, this.ruta, directorio.trim().trim().replace("//", "/"));

			if (!verificaExiste(sftp, path)) {
				mkdirs(sftp, path);
			}
		} catch (Exception ex) {
			log.error(String.format("Ocurrió un error al crear el directorio %s en el servidor FTP.", directorio.trim()), ex);
			throw new InternalException(String.format("Ocurrió un error al crear el directorio %s en el servidor FTP.", directorio.trim()));
		} finally {
			pool.returnObject(sftp);
		}
	}

	/**
	 * Eliminar directorio
	 * @param directorio directorio remoto
	 */
	public void elimarDirectorio(String directorio) throws InternalException {
		ChannelSftp sftp = pool.borrowObject();

		try {
			String path = String.format(PATH, this.ruta, directorio.trim().trim().replace("//", "/"));

			if (verificaExiste(sftp, path)) {
				sftp.rmdir(path);
			}
		} catch (Exception ex) {
			throw new InternalException(String.format("Ocurrió un error al eliminar el directorio %s en el servidor FTP.", directorio.trim()));
		} finally {
			pool.returnObject(sftp);
		}
	}

	/**
	 * Cambiar directorio
	 * @param directorio directorio remoto
	 */
	public void cambiarDirectorio(String directorio) throws InternalException {
		ChannelSftp sftp = pool.borrowObject();

		try {
			String path = String.format(PATH, this.ruta, directorio.trim().trim().replace("//", "/"));

			if (verificaExiste(sftp, path)) {
				sftp.cd(path);
			}
		} catch (Exception ex) {
			log.error("Ocurrió un error al cambiar de directorio en el servidor FTP.", ex);
			throw new InternalException("Ocurrió un error al cambiar de directorio en el servidor FTP.");
		} finally {
			pool.returnObject(sftp);
		}

	}

	/**
	 * Descargar archivo
	 * @param directorio directorio remoto
	 * @param nombre nombre de archivo remoto
	 * @return InputStream de archivo
	 */
	public byte[] descargaArchivo(String directorio, String nombre) throws InternalException {
		ChannelSftp sftp = pool.borrowObject();

		try {
			String path = String.format(PATH, this.ruta, directorio.trim().trim().replace("//", "/"));

			if (verificaExiste(sftp, path)) {
				sftp.cd(path);

				return IOUtils.toByteArray(sftp.get(nombre.trim().replace("//", "/")));
			} else {
				return new byte[0];
			}
		} catch (SftpException | IOException ex) {
			log.error(String.format("Ocurrió un error al descargar el archivo %s del servidor FTP.", nombre.trim()), ex);
			throw new InternalException(String.format("Ocurrió un error al descargar el archivo %s del servidor FTP.", nombre.trim()), ex);
		} finally {
			pool.returnObject(sftp);
		}
	}

	/**
	 * Subir archivo
	 * @param directorio directorio remoto
	 * @param nombre nombre de archivo remoto
	 * @param archivo input stream
	 */
	public void subirArchivo(String directorio, String nombre, InputStream archivo) throws InternalException {
		ChannelSftp sftp = pool.borrowObject();

		try {
			String path = String.format(PATH, this.ruta, directorio.trim().trim().replace("//", "/"));

			if (!verificaExiste(sftp, path)) {
				mkdirs(sftp, path);
			}

			if (verificaExiste(sftp, path)) {
				sftp.cd(path);
				sftp.put(archivo, nombre.trim());
			}
		} catch (SftpException ex) {
			log.error(String.format("Ocurrió un error al subir el archivo %s en el servidor FTP.", nombre.trim()), ex);
			throw new InternalException(String.format("Ocurrió un error al subir el archivo %s en el servidor FTP.", nombre.trim()));
		} finally {
			pool.returnObject(sftp);
		}
	}

	/**
	 * Borrar archivos
	 * @param directorio directorio remoto
	 * @param nombre nombre de archivo
	 */
	public void borrarArchivo(String directorio, String nombre) throws InternalException {
		ChannelSftp sftp = pool.borrowObject();

		try {
			String path = String.format(PATH, this.ruta, directorio.trim().trim().replace("//", "/"));

			if (verificaExiste(sftp, path)) {
				sftp.cd(directorio.trim());
				sftp.rm(nombre.trim());
			}
		} catch (SftpException ex) {
			log.error(String.format("Ocurrió un error al eliminar archivo %s en el servidor FTP.", nombre.trim()), ex);
			throw new InternalException(String.format("Ocurrió un error al eliminar archivo %s en el servidor FTP.", nombre.trim()));
		} finally {
			pool.returnObject(sftp);
		}
	}

	/**
	 * Crear recursivamente directorios de varios niveles
	 * @param directorio directorio multinivel
	 */
	private void mkdirs(ChannelSftp sftp, String directorio) throws InternalException {
		String[] carpetas = directorio.split("/");

		try {
			sftp.cd("/");
			for (String carpeta: carpetas) {
				if (carpeta.length() > 0) {
					try {
						sftp.cd(carpeta);
					} catch (Exception e) {
						sftp.mkdir(carpeta);
						sftp.cd(carpeta);
					}
				}
			}
		} catch (SftpException e) {
			throw new InternalException("Error en la creación de los directorios.", e);
		}
	}

	/**
	 * Verifica si existe directorio o archivo
	 * @param sftp Canal de conexión sftp
	 * @param directorio directorio remoto
	 */
	public boolean verificaExiste(ChannelSftp sftp , String directorio) {
		try {
			String creacion = sftp.stat(directorio).getAtimeString();

			return (null != creacion && !creacion.trim().isEmpty());
		} catch (SftpException e) {
			return false;
		}
	}
}
