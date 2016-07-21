package io.summerx.sso.authentication.userdetails;

/**
 * 请在这里添加类说明.
 *
 * @author summerx
 * @Date 2016-07-15 10:41 AM
 */
public class DefaultUserDetails implements UserDetails {

    private final String username;
    private String password;
    private final boolean accountLocked;
    private final boolean passwordExpired;

    public DefaultUserDetails(String username, String password) {
        this(username, password, false, false);
    }

    public DefaultUserDetails(String username, String password, boolean accountLocked, boolean passwordExpired) {
        this.username = username;
        this.password = password;
        this.accountLocked = accountLocked;
        this.passwordExpired = passwordExpired;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountLocked() {
        return accountLocked;
    }

    @Override
    public boolean isPasswordExpired() {
        return passwordExpired;
    }
}
