package mx.gob.queretaro.services.util;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mx.gob.queretaro.repositories.exception.InternalException;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Properties;

@EqualsAndHashCode(callSuper = true)
@Data
public class SftpFactory extends BasePooledObjectFactory<ChannelSftp> {

	private SftpProperties properties;

	public SftpFactory(SftpProperties properties) {
		this.properties = properties;
	}

	@Override
	public ChannelSftp create() throws Exception {
		try {
			JSch jsch = new JSch();
			Properties sshConfig = new Properties();

			Session sshSession = jsch.getSession(properties.getUsuario(), properties.getServidor(), properties.getPuerto());
			sshSession.setPassword(properties.getContrasena());
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			ChannelSftp channel = (ChannelSftp) sshSession.openChannel("sftp");
			channel.connect();

			return channel;
		} catch (JSchException e) {
			throw new InternalException("Ocurrió un error en la conexión al SFTP.", e);
		}
	}

	@Override
	public PooledObject<ChannelSftp> wrap(ChannelSftp channelSftp) {
		return new DefaultPooledObject<>(channelSftp);
	}

	@Override
	public void destroyObject(PooledObject<ChannelSftp> p) {
		ChannelSftp channelSftp = p.getObject();
		channelSftp.disconnect();
	}
}
