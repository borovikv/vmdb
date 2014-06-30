package md.varoinform.sequrity.exception;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 12/14/13
 * Time: 12:29 PM
 */
public class RegistrationException extends Throwable {
    public enum Error {
        CONNECTION_ERROR("CONNECTION ERROR"), RESPONSE_ERROR("SERVER ERROR");
        private final String text;

        Error(String text) {
            this.text = text;
        }

        public static Error parseError(String str){
            //ToDo: real empl
            return null;
        }
    }

    private Error error;

    public RegistrationException(Error error) {
        super(error.text);
        this.error = error;
    }

    public RegistrationException(Error error, IOException e) {
        super(error.text, e);
        this.error = error;
    }

    public RegistrationException(Throwable cause) {
        super(cause);
    }

    public Error getError() {
        return error;
    }

}
