package md.varoinform;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 8/18/14
 * Time: 4:10 PM
 */
public class ClosableTest {
    static class ClosableImpl implements AutoCloseable{

        @Override
        public void close()  {
            System.out.println("close");
        }

    }


    public static void main(String[] args) {
        //noinspection UnusedDeclaration
        try (ClosableImpl cls = new ClosableImpl()){
            System.out.println("try");
            //noinspection UnnecessaryReturnStatement
            return;
        }
    }
}
