package mx.gob.queretaro.services.util;

import java.security.cert.X509Certificate;
import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.repositories.exception.InternalException;
import mx.gob.queretaro.repositories.repository.IParametroRepository;

@Slf4j
@Component
public class Apache {

	private final IParametroRepository parametroRepository;
	private String servidor;
	private String usuario;
	private String contrasena;
	private String ruta;

	@Autowired
	public Apache(IParametroRepository parametroRepository){
		this.parametroRepository = parametroRepository;
	}

	@PostConstruct
	public void init() {
		/*try {
			this.servidor = parametroRepository.obtenerPorNombre("APACHE_SERVIDOR", "AC").getValor().trim();
			this.usuario = parametroRepository.obtenerPorNombre("APACHE_USUARIO", "AC").getValor().trim();
			this.contrasena = Aes.desencriptar(parametroRepository.obtenerPorNombre("APACHE_CONTRASENA", "AC").getValor().trim(), null);
			this.ruta = parametroRepository.obtenerPorNombre("APACHE_RUTA", "AC").getValor().trim();
		} catch (InternalException ex) {
			log.error(ex.getMessage(), Sftp.class);
		}*/
	}

	public byte[] descargaArchivo(String txtUbicacion, String txtArchivo) throws InternalException {
		try {
			byte[] resultado;
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
			BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.usuario, this.contrasena));
			HttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(connectionFactory).setDefaultCredentialsProvider(credentialsProvider).build();
			ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			ResponseEntity<byte[]> response = restTemplate.getForEntity("https://" + servidor + ruta + txtUbicacion + "/" + txtArchivo, byte[].class);

			if (response.getStatusCode() == HttpStatus.OK) {
				if (response.getBody() != null) {
					resultado = response.getBody();
				} else {
					throw new InternalException("El documento " + txtArchivo + " no existe en el repositorio.");
				}
			} else {
				throw new InternalException("Ocurrió un error al conectarse al conectarse con el repositorio de documentos, estatus: " + response.getStatusCode() + ".");
			}

			return resultado;
		}catch (RuntimeException ex) {
			throw new InternalException("No se encontraron resultados");
		} catch (Exception ex) {
			log.error("Ocurrió un error al conectarse a con el repositorio de documentos.", ex);
			throw new InternalException("Ocurrió un error al conectarse a con el repositorio de documentos.");
		}
	}

}
