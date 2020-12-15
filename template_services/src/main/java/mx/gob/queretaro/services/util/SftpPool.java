package mx.gob.queretaro.services.util;

import com.jcraft.jsch.ChannelSftp;
import lombok.Data;
import mx.gob.queretaro.repositories.exception.InternalException;
import org.apache.commons.pool2.impl.GenericObjectPool;

@Data
public class SftpPool {
	private GenericObjectPool<ChannelSftp> pool;

	public SftpPool(SftpFactory factory) {
		this.pool = new GenericObjectPool<>(factory, factory.getProperties().getPool());
	}

	/**
	 * Obtiene un objeto de conexión sftp
	 * @return objeto de conexión sftp
	 */
	public ChannelSftp borrowObject() throws InternalException {
		try {
			return pool.borrowObject();
		} catch (Exception e) {
			throw new InternalException("Ocurrió un error en la conexión al SFTP", e);
		}
	}

	/**
	 * Devuelve un objeto de conexión sftp
	 * @param channelSftp objeto de conexión sftp
	 */
	public void returnObject(ChannelSftp channelSftp) {
		if (channelSftp!=null) {
			pool.returnObject(channelSftp);
		}
	}
}
