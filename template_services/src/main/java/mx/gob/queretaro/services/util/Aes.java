package mx.gob.queretaro.services.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
import mx.gob.queretaro.repositories.exception.InternalException;

@Slf4j
public class Aes {

	private static SecretKeySpec secreta;
	protected static final String PASS = "pr0y3ct0$$pf";

	private Aes () {
	}

	private static void creaLlave(String pass) throws InternalException {
		try {
			MessageDigest sha = null;
			byte[] llave = pass.trim().getBytes(StandardCharsets.UTF_8);
			sha = MessageDigest.getInstance("SHA-1");
			llave = sha.digest(llave);
			llave = Arrays.copyOf(llave, 16);
			secreta = new SecretKeySpec(llave, "AES");
		} catch (NoSuchAlgorithmException ex) {
			log.error("Ocurrió un error en la generación de llave para encriptar.", ex);
			throw new InternalException("Ocurrió un error en la generación de llave para encriptar.", Aes.class);
		}
	}

	public static String encriptar(String cadena, String pass) throws InternalException {
		String resultado;

		try {
			if (pass == null) {
				pass = PASS;
			}

			creaLlave(pass.trim());
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secreta);

			resultado = Base64.getEncoder().encodeToString(cipher.doFinal(cadena.trim().getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
			log.error("Ocurrió un error al encriptar la cadena. ", ex);
			throw new InternalException("Ocurrió un error al encriptar la cadena. ", Aes.class);
		}

		return resultado;
	}

	public static String desencriptar(String cadena, String pass) throws InternalException {
		String resultado;

		try {
			if (pass == null) {
				pass = PASS;
			}

			creaLlave(pass.trim());
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secreta);

			resultado = new String(cipher.doFinal(Base64.getDecoder().decode(cadena)));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
			log.error("Ocurrió un error al des encriptar la cadena. ", ex);
			throw new InternalException("Ocurrió un error al des encriptar la cadena. ", Aes.class);
		}

		return resultado;
	}

}
