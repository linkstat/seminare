package ar.com.hmu.exceptions;

public class DatabaseAuthenticationException extends RuntimeException {

    public DatabaseAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

}