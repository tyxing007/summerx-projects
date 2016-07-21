package io.summerx.sso.authentication.exception;

/**
 * Created by xiayg on 7/11/2016.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthenticationException(String msg) {
        super(msg);
    }
}
