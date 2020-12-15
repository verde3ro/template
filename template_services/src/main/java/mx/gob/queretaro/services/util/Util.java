package mx.gob.queretaro.services.util;

import java.io.Serializable;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import mx.gob.queretaro.repositories.exception.InternalException;

@Component
public class Util implements Serializable{

	private static final long serialVersionUID = 745605656014856903L;

	public static String capsFirst(String str) {
		String[] words = str.split(" ");
		StringBuilder ret = new StringBuilder();

		for (int i = 0; i < words.length; i++) {
			ret.append(Character.toUpperCase(words[i].charAt(0)));
			ret.append(words[i].substring(1));
			if (i < words.length - 1) {
				ret.append(' ');
			}
		}
		return ret.toString();
	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}

		return true;
	}

	public static boolean buscaCadena(String cadena1, String cadena2) {
		String[] arreglo1 = cadena1.split(",");
		String[] arreglo2 = cadena2.split(",");
		for (String c1 : arreglo1) {
			for (String c2 : arreglo2) {
				if (c1.equals(c2)) {
					return true;
				}
			}
		}
		return false;
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static String getExtension(String fileName) throws InternalException {
		try {
			String extension = FilenameUtils.getExtension(fileName);
			extension = "." + extension;
			return extension;
		} catch (Exception ex) {
			throw new InternalException("Ocurrio un error al obtener la extension del archivo");
		}
	}

	public static String getErrorsFieldsForm(BindingResult errores) {
		StringBuilder mensaje = new StringBuilder();

		for (FieldError error : errores.getFieldErrors()) {
			String campo = error.getField().trim() + " " + error.getDefaultMessage().trim().replaceAll("null", "nulo") + ".";

			if (mensaje.toString().trim().isEmpty()) {
				mensaje.append(campo);
			} else {
				mensaje.append("<br />").append(campo);
			}
		}
		return mensaje.toString();
	}
	
	public static String getErrorTransactionSave(Exception ex, String objeto) {
		StringBuilder mensaje = new StringBuilder();
		if(ex.getCause().toString().contains("ConstraintViolationException")) {
			mensaje.append(objeto + " - La información ingresada ya se encuentra registrada.");
		} else {
			mensaje.append(objeto + " - Oucrrio un error al guardar el registro.");
		}
		
		return mensaje.toString();
	}
	
}
