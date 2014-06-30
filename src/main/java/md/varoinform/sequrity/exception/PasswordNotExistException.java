package md.varoinform.sequrity.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/13/13
 * Time: 9:19 AM
 */
public class PasswordNotExistException extends Throwable {
    public static final String PASSWORD_NOT_EXIST_EXCEPTION = "key not exit";

    public PasswordNotExistException(String message) {
        super(message);
    }
}
