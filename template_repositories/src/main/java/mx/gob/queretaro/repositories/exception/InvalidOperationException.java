package mx.gob.queretaro.repositories.exception;

public class InvalidOperationException extends RuntimeException{

	private static final long serialVersionUID = -675394868598002646L;
	private String code;
	private String resource;

	public InvalidOperationException(String message) {
		super(message);
	}

	public InvalidOperationException(String message, String code, String resource) {
		super(message);
		this.code = code;
		this.resource = resource;
	}

	public String getCode() {
		return code;
	}

	public String getResource() {
		return resource;
	}
}
