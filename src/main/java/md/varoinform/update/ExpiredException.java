package md.varoinform.update;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 7/8/14
 * Time: 11:35 AM
 */
public class ExpiredException extends Throwable {
    public ExpiredException(String group) {
        super(group);
    }
}
