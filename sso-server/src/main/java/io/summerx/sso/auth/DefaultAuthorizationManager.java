package io.summerx.sso.auth;

import io.summerx.framework.cache.CustomizedCache;
import io.summerx.framework.utils.StringUtils;
import io.summerx.sso.auth.exception.AccountLockedException;
import io.summerx.sso.auth.exception.BadCredentialsException;
import io.summerx.sso.auth.exception.CredentialsExpiredException;
import io.summerx.sso.auth.exception.UsernamePasswordEmptyException;
import io.summerx.sso.auth.service.UserObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.concurrent.ExecutorService;

/**
 * Created by xiayg on 7/11/2016.
 */
public class DefaultAuthorizationManager implements AuthorizationManager {

    static final int TGT_EXP = 3600;
    static final int ST_EXP = 60;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService executorService;

    // 用于缓存用户，Token信息
    private CustomizedCache ssoCache;

    // 用于获取用户信息
    private UserObjectService userService;

    @Override
    public UsernamePasswordAuthorization authenticate(UsernamePasswordCredentials credentials) {
        if (credentials == null) {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }

        // 用户名密码为空
        if (StringUtils.isEmpty(credentials.getUsername()) || StringUtils.isEmpty(credentials.getPassword())) {
            throw new UsernamePasswordEmptyException(String.format("用户名或密码为空", credentials.getUsername()));
        }

        // 根据用户名获取用户信息
        UserObject user = userService.loadUserByUsername(credentials.getUsername());
        // 用户不存在
        if (user == null) {
            throw new UsernameNotFoundException(String.format("用户[name=%s]不存在", credentials.getUsername()));
        }
        // 用户已经被锁定
        if (user.isAccountLocked()) {
            throw new AccountLockedException(String.format("用户[name=%s]已经被锁定", credentials.getUsername()));
        }
        // 验证密码
        String encryptPassword = credentials.getPassword();
        if (encryptPassword == null || !encryptPassword.equals(user.getPassword())) {
            throw new BadCredentialsException(String.format("用户[name=%s]密码错误", credentials.getUsername()));
        }
        // 密码已过期
        if (user.isPasswordExpired()) {
            throw new CredentialsExpiredException(String.format("用户[name=%s]密码过期", credentials.getUsername()));
        }

        // TODO 校验验证码

        // 验证通过，创建登录用户授权凭证
        UsernamePasswordAuthorization authorization = new UsernamePasswordAuthorization(user.getUsername(), credentials.getClientInfo());
        // 生成Tgt
        authorization.setTgt(SSOHelper.generateTgt(authorization));
        // FIXME 缓存
        // ((RedisOperations) ssoCache.getNativeCache()).opsForValue().set(authorization.getTgt(), authorization, 40000, TimeUnit.SECONDS);
        ssoCache.put(authorization.getTgt(), authorization, TGT_EXP);

        return authorization;
    }

    @Override
    public UsernamePasswordAuthorization authenticate(TicketCredentials credentials) {
        if (credentials == null || credentials.getTicket() == null) {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }

        // TGT + ClientInfo
        UsernamePasswordAuthorization authorization = ssoCache.get(credentials.getTicket(), UsernamePasswordAuthorization.class);
        if (authorization == null) {
            return null;
        }

        // TODO 校验IP，user-agent，refer是否一致

        return authorization;
    }

    @Override
    public String issueSt(UsernamePasswordAuthorization authorization, String app) {
        // 生成ST
        String st = SSOHelper.generateSt(authorization);
        // FIXME 缓存ST
//        ((RedisOperations) ssoCache.getNativeCache()).opsForValue().set(st, authorization.getTgt(), ST_EXP, TimeUnit.SECONDS);
        ssoCache.put(st, authorization.getTgt(), ST_EXP);

        return st;
    }

    @Override
    public UsernamePasswordAuthorization validateSt(String st, String appname) {
        // 获取颁发ST的TGT
        String tgt = ssoCache.get(st, String.class);
        if (tgt == null) {
            return null;
        }
        // 清除ST（只能使用一次）
        ssoCache.evict(st);

        // 根据TGT获取用户登录凭证
        UsernamePasswordAuthorization authorization = ssoCache.get(tgt, UsernamePasswordAuthorization.class);
        if (authorization == null) {
            return  null;
        }

        // 如果该应用已经被授权过了，清除原来的授权（或等其自己过期?）
        GrantedApplication granted = authorization.getApplication(appname);
        if (granted != null) {
            // 从授权应用中去除
            authorization.removeApplication(appname);
        }

        // 创建一个新的应用授权
        GrantedApplication app = new GrantedApplication(appname);
        // 加入授权应用
        authorization.addApplication(app);
        // 重新缓存
        ssoCache.put(authorization.getTgt(), authorization, TGT_EXP);

        return authorization;
    }

    @Override
    public void destoryTgt(String tgt) {
        UsernamePasswordAuthorization authorization = ssoCache.get(tgt, UsernamePasswordAuthorization.class);;
        if (authorization == null) {
            return;
        }
        // 通知应用登出
        if (authorization.getApplications() != null) {
            for (final GrantedApplication granted : authorization.getApplications()) {
                executorService.execute(() -> {
                    // TODO 调用回调服务
                    if (!StringUtils.isEmpty(granted.getCallback())) {
                        logger.info(String.format("notify application[%s] to logout", granted.getName()));
                    }
                });
            }
        }
        // 清除TGT
        ssoCache.evict(tgt);
    }

    @Override
    public boolean accessiable(UsernamePasswordAuthorization authentication, String appname) {
        return true;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setSsoCache(CustomizedCache ssoCache) {
        this.ssoCache = ssoCache;
    }

    public void setUserService(UserObjectService userService) {
        this.userService = userService;
    }
}
