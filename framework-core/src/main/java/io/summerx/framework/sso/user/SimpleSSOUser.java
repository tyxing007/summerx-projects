package io.summerx.framework.sso.user;

/**
 * <p>
 * 一个最简单的SSO用户类
 * </p>
 *
 * @author summerx
 * @Date 2016-07-19 4:02 PM
 */
public class SimpleSSOUser implements SSOUser, java.io.Serializable {

    // 用户名
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
