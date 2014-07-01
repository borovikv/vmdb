package md.varoinform.sequrity.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/12/13
 * Time: 5:14 PM
 */
public class PasswordException extends Throwable {
    private final Error type;

    public PasswordException(Error type) {
        super(type.getText());
        this.type = type;
    }

    public Error getType() {
        return type;
    }
}
