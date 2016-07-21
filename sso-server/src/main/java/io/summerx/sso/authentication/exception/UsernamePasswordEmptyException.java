package io.summerx.sso.authentication.exception;

/**
 * Created by xiayg on 7/11/2016.
 */
public class UsernamePasswordEmptyException extends AuthenticationException {

    public UsernamePasswordEmptyException(String msg, Throwable t) {
        super(msg, t);
    }

    public UsernamePasswordEmptyException(String msg) {
        super(msg);
    }
}
