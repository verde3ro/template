package mx.gob.queretaro.repositories.exception;

/**
 *
 * @author rverde
 */
public class NotFoundException extends InternalException {

    private static final long serialVersionUID = -723359057241080648L;

    public NotFoundException(final String message, final Object... params) {
	super(message, params);
    }

    public NotFoundException(final String message, final Throwable cause, final Object... params) {
	super(message, cause, params);
    }

}
