package xyz.luan.sabv;

/**
 * Thrown when the validation annotations are used incorrectly in a class.
 * @author luan.nico
 *
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -967219229540851606L;

    public ValidationException(String message) {
        super(message);
    }
}
