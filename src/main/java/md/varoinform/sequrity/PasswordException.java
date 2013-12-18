package md.varoinform.sequrity;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/12/13
 * Time: 5:14 PM
 */
public class PasswordException extends Throwable {
    public static final String VALIDATION_ERROR = "validation error";
    public static final String DECRYPTION_ERROR = "decryption error";

    public PasswordException(String message) {
        super(message);
    }

    public PasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
