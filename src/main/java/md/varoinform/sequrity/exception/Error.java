package md.varoinform.sequrity.exception;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 7/1/14
* Time: 11:05 AM
*/
public enum Error {
    CONNECTION_ERROR("CONNECTION ERROR"),
    RESPONSE_ERROR("SERVER ERROR"),
    VALIDATION_ERROR("validation error"),
    PASSWORD_NOT_EXIST_ERROR("key not exit");
    private final String text;

    Error(String text) {
        this.text = text;
    }

    public static Error parseError(@SuppressWarnings("UnusedParameters") String str){
        //ToDo: real empl
        return null;
    }

    public String getText() {
        return text;
    }
}
