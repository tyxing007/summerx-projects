package io.summerx.framework.sso.user;

/**
 * <p>
 * 线程级别持有SSO用户
 * </p>
 *
 * @author summerx
 * @Date 2016-07-19 4:05 PM
 */
public class SSOUserHolder {


    private static final ThreadLocal<SSOUser> context = new ThreadLocal<SSOUser>();

    public static <T extends SSOUser> T get() {
        return (T) context.get();
    }

    public static <T extends SSOUser> void set(T user) {
        context.set(user);
    }

    public static void remove() {
        context.remove();
    }
}
