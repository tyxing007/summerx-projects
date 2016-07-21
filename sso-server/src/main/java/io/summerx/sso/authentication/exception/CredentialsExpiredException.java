package io.summerx.sso.authentication.exception;

/**
 * Created by xiayg on 7/11/2016.
 */
public class CredentialsExpiredException extends AuthenticationException {

    public CredentialsExpiredException(String msg, Throwable t) {
        super(msg, t);
    }

    public CredentialsExpiredException(String msg) {
        super(msg);
    }
}
