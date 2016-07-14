package io.summerx.sso.client;

/**
 * 线程级别持有用户信息
 *
 * @author summerx
 * @Date 2016-07-14 1:07 PM
 */
public class UserObject implements java.io.Serializable {

    // 用户名
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
