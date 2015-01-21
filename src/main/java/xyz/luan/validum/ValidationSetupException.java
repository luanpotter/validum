package xyz.luan.validum;

/**
 * Thrown when the validation annotations are used incorrectly in a class.
 * @author luan.nico
 *
 */
public class ValidationSetupException extends RuntimeException {

    private static final long serialVersionUID = -967219229540851606L;

    public ValidationSetupException(String message) {
        super(message);
    }
}
