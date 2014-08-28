package md.varoinform.sequrity.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/28/14
 * Time: 10:08 AM
 */
public class LockedException extends Throwable {
    private final Error messageError;

    public LockedException(Error lockedError) {
        messageError = lockedError;
    }

    @Override
    public String getMessage() {
        return messageError.getText();
    }
}
