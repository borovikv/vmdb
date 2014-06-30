package md.varoinform.sequrity.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/12/13
 * Time: 5:14 PM
 */
public class PasswordException extends Throwable {
    public static final String VALIDATION_ERROR = "validation error";
    @SuppressWarnings("UnusedDeclaration")
    public static final String DECRYPTION_ERROR = "decryption error";

    public PasswordException(String message) {
        super(message);
    }
}
