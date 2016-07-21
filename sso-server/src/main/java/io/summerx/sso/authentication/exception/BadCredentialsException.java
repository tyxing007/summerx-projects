package io.summerx.sso.authentication.exception;

/**
 * Created by xiayg on 7/11/2016.
 */
public class BadCredentialsException extends AuthenticationException {

    public BadCredentialsException(String msg, Throwable t) {
        super(msg, t);
    }

    public BadCredentialsException(String msg) {
        super(msg);
    }
}
