package io.summerx.sso.auth;

/**
 * 验证用户名密码
 * 生成tgt
 * 验证tgc
 * 生成st
 * 验证st
 * 生成pt
 *
 */
public interface AuthorizationManager {

    // 密钥
    public static final String JWT_KEY = "superjwtkey";

    // 默认过期时间
    public static final long EXP_MILLS = 7 * 1000 * 60 * 60 * 24l;

    /**
     * 验证[用户名，密码，验证码]，验证成功返回已授权的凭证
     * @param credentials [用户名，密码，验证码，客户端信息]
     * @return
     */
    UsernamePasswordAuthorization authenticate(UsernamePasswordCredentials credentials);

    /**
     * 验证TGT，验证成功返回已授权的凭证
     * @param credentials
     * @return
     */
    UsernamePasswordAuthorization authenticate(TicketCredentials credentials);

    /**
     * 根据客户端传过来的Cookie（tgt）获取已授权的凭证
     * @param tgt
     * @return
     */
//    UsernamePasswordAuthorization getAuthorization(String tgt);

    /**
     * 颁发一个st，签发的st只能使用一次，且存活时间尽量短。
     *
     * @param authentication
     * @return
     */
     String issueSt(UsernamePasswordAuthorization authentication, String app);

    /**
     * 校验st的合法性，如果合法，则签发一个pt
     *
     * @param st
     * @return
     */
     UsernamePasswordAuthorization validateSt(String st, String app);

    /**
     * 销毁tgt，tgt销毁后，需要通知应用销毁各自的pt
     * @param tgt
     */
     void destoryTgt(String tgt);

    /**
     * 判断已授权的凭证中是否允许访问指定应用资源
     * @param authentication
     * @param appname
     * @return
     */
     boolean accessiable(UsernamePasswordAuthorization authentication, String appname);
}
