package io.summerx.sso.authentication.exception;

/**
 * Created by xiayg on 7/11/2016.
 */
public class UsernameNotFoundException extends AuthenticationException {

    public UsernameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public UsernameNotFoundException(String msg) {
        super(msg);
    }
}
