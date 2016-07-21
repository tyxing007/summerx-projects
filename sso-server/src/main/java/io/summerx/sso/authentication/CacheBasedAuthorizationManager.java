package io.summerx.sso.authentication;

import io.summerx.framework.cache.CustomizedCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 基于Cache的认知管理器
 * </p>
 *
 * @author summerx
 * @Date 2016-07-19
 */
public class CacheBasedAuthorizationManager extends AbstractAuthorizationManager implements AuthorizationManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // SSO服务器端存放用户身份信息的缓存
    private CustomizedCache ssoCache;

    // 应用端存放用户身份信息的缓存（可以直接通过清除PT对应的缓存来达到退出应用）
    private CustomizedCache userCache;

    // TGT过期时间，默认60分钟
    private int tgtExpires = 3600;

    // ST过期时间，默认60秒
    private int stExpires = 60;

    /**
     * 保存用户身份信息
     * @param authorization
     */
    protected void storeCurrentAuthorization(UserAuthorization authorization) {
        ssoCache.put(authorization.getTicket(), authorization, tgtExpires);
    }

    /**
     * 根据TGT凭证获取用户身份信息
     * @param credentials
     * @return
     */
    protected UserAuthorization getCurrentAuthorization(TgtCredentials credentials) {
        return ssoCache.get(credentials.getTicket(), UserAuthorization.class);
    }

    /**
     * 保存临时登录凭证（ST凭证）
     */
    protected void storeProvisionalCertificate(ProvisionalCredentials credentials, UserAuthorization authorization) {
        ssoCache.put(credentials.getTicket(), authorization.getTicket(), stExpires);
    }

    /**
     * 验证临时登录凭证（ST凭证）
     * @return 身份信息
     */
    protected UserAuthorization getCurrentAuthorization(ProvisionalCredentials credentials) {
        // 获取颁发ST的TGT凭证
        String tgt = ssoCache.get(credentials.getTicket(), String.class);
        if (tgt == null) {
            return null;
        }
        // 清除ST（只能使用一次）
        ssoCache.evict(credentials.getTicket());

        return ssoCache.get(tgt, UserAuthorization.class);
    }

    /**
     * 清除用户身份信息
     * @param authorization
     */
    protected void removeCurrentAuthorization(final UserAuthorization authorization) {
        // 清除TGT
        ssoCache.evict(authorization.getTicket());
    }

    /**
     * 通知应用
     * @param application
     * @param action
     */
    protected void notifyApplication(GrantedApplication application, String action) {
        if (userCache != null && application.getTikcet() != null) {
            userCache.evict(application.getTikcet());
        }
    }

    public void setSsoCache(CustomizedCache ssoCache) {
        this.ssoCache = ssoCache;
    }

    public void setUserCache(CustomizedCache userCache) {
        this.userCache = userCache;
    }

    public void setTgtExpires(int tgtExpires) {
        this.tgtExpires = tgtExpires;
    }

    public void setStExpires(int stExpires) {
        this.stExpires = stExpires;
    }
}
