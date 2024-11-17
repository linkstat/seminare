package ar.com.hmu.exceptions;

public class ServiceException extends Exception {

    public ServiceException(String message, Throwable cause) {
        super("Excepción personalizada en ServiceException: \n" + message, cause);
    }

}

