package md.varoinform.sequrity.exception;

/**
* Created with IntelliJ IDEA.
* User: Vladimir Borovic
* Date: 7/1/14
* Time: 11:05 AM
*/
public enum Error {
    CONNECTION_ERROR("connection_error"),  // Registration exception
    RESPONSE_ERROR("server_error"),         // Registration exception
    VALIDATION_ERROR("validation_error"),
    DECRYPT_ERROR("decrypt_error"),
    INVALID_UID("invalid_uid"), // Registration exception
    EXPIRED_UID("expired_uid");
    private final String text;

    Error(String text) {
        this.text = text;
    }

    public static Error parseError(String error){
        switch (error){
            case "INVALID_UID": return INVALID_UID;
            case "EXPIRED_UID": return EXPIRED_UID;
            default: return RESPONSE_ERROR;
        }
    }

    public String getText() {
        return text;
    }
}
