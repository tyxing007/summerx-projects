package io.summerx.sso.authentication.userdetails;

/**
 * 请在这里添加类说明.
 *
 * @author summerx
 * @Date 2016-07-15 10:40 AM
 */
public interface UserDetails extends java.io.Serializable {

    String getUsername();

    String getPassword();

    boolean isAccountLocked();

    boolean isPasswordExpired();
}
