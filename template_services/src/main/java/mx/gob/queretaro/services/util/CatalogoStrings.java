package mx.gob.queretaro.services.util;

import java.io.Serializable;

public class CatalogoStrings implements Serializable{
	private static final long serialVersionUID = 1L;

	protected static final String SUCCESS = "success";
	protected static final String ERROR = "error";
	protected static final String WARNING = "warning";
	protected static final String INFO = "info";
	protected static final String QUESTION = "question";
	protected static final String USUARIO = "usuario";

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static String getSuccess() {
		return SUCCESS;
	}
	public static String getError() {
		return ERROR;
	}
	public static String getWarning() {
		return WARNING;
	}
	public static String getInfo() {
		return INFO;
	}
	public static String getQuestion() {
		return QUESTION;
	}
	public static String getUsuario() {
		return USUARIO;
	}
}
