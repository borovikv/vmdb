package md.varoinform.sequrity.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 3/28/14
 * Time: 11:41 AM
 */
public class UnregisteredDBExertion extends Throwable {
    String messageKey = "unregistered_program_error";

    @Override
    public String getMessage() {
        return messageKey;
    }
}
