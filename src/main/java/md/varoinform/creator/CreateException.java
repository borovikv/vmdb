package md.varoinform.creator;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/19/14
 * Time: 2:45 PM
 */
public class CreateException extends Throwable {
    public CreateException(Throwable cause) {
        super(cause);
    }

    public CreateException(String message) {
        super(message);
    }
}
