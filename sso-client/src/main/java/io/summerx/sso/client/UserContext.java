package io.summerx.sso.client;

/**
 * 线程级别持有用户信息
 *
 * @author summerx
 * @Date 2016-07-14 1:07 PM
 */
public class UserContext {

    private static final ThreadLocal<UserObject> context = new ThreadLocal<UserObject>();

    public static <T extends UserObject> T get() {
        return (T) context.get();
    }

    public static <T extends UserObject> void set(T user) {
        context.set(user);
    }

    public static void remove() {
        context.remove();
    }
}
