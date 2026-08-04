package io.jatinjindal.backend.exception;

public class WindowsLensException extends RuntimeException {

    public WindowsLensException(String message, Throwable cause) {
        super(message, cause);
    }

    public WindowsLensException(String message) {
        super(message);
    }
}
