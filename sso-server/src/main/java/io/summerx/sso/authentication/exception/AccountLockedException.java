package io.summerx.sso.authentication.exception;

/**
 * Created by xiayg on 7/11/2016.
 */
public class AccountLockedException extends AuthenticationException {

    public AccountLockedException(String msg, Throwable t) {
        super(msg, t);
    }

    public AccountLockedException(String msg) {
        super(msg);
    }
}
