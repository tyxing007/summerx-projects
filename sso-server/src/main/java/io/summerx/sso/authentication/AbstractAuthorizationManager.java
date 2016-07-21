package io.summerx.sso.authentication;

import io.summerx.framework.codec.TextEncodeHelper;
import io.summerx.framework.utils.StringUtils;
import io.summerx.sso.authentication.exception.*;
import io.summerx.sso.authentication.generator.RandomTicketGenerator;
import io.summerx.sso.authentication.generator.TicketGenerator;
import io.summerx.sso.authentication.service.UserService;
import io.summerx.sso.authentication.userdetails.UserDetails;
import io.summerx.sso.constants.SSOServerContants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Created by xiayg on 7/11/2016.
 */
public abstract class AbstractAuthorizationManager implements AuthorizationManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService executorService;

    // 用于获取用户信息
    private UserService userService;

    // 用于生成TGT，ST和PT
    private TicketGenerator generator = new RandomTicketGenerator();

    /**
     *
     * 验证用户名密码信息，成功则返回用户身份信息
     * @param credentials 用户登录信息
     * @return 用户身份信息
     */
    @Override
    public UserAuthorization authenticate(UsernamePasswordCredentials credentials) {
        if (credentials == null) {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }

        // 用户名密码为空
        if (StringUtils.isEmpty(credentials.getUsername()) || StringUtils.isEmpty(credentials.getPassword())) {
            throw new UsernamePasswordEmptyException(String.format("用户名或密码为空", credentials.getUsername()));
        }

        // 根据用户名获取用户信息
        UserDetails user = userService.loadUserByUsername(credentials.getUsername());
        // 用户不存在
        if (user == null) {
            throw new UsernameNotFoundException(String.format("用户[name=%s]不存在", credentials.getUsername()));
        }
        // 用户已经被锁定
        if (user.isAccountLocked()) {
            throw new AccountLockedException(String.format("用户[name=%s]已经被锁定", credentials.getUsername()));
        }
        // 验证密码
        String encryptPassword = passwordEncode(credentials.getPassword(), null);
        if (encryptPassword == null || !encryptPassword.equals(user.getPassword())) {
            throw new BadCredentialsException(String.format("用户[name=%s]密码错误", credentials.getUsername()));
        }
        // 密码已过期
        if (user.isPasswordExpired()) {
            throw new CredentialsExpiredException(String.format("用户[name=%s]密码过期", credentials.getUsername()));
        }

        // TODO 校验验证码

        // 验证通过，创建用户身份信息
        UserAuthorization authorization = new UserAuthorization(user.getUsername(), generator, credentials.getClientInfo());
        // 保存用户身份信息
        storeCurrentAuthorization(authorization);

        // 返回用户身份信息
        return authorization;
    }

    /**
     * 验证TGT凭证，成功则返回授权的用户身份信息
     * @param credentials 用户TGT信息
     * @return
     */
    @Override
    public UserAuthorization authenticate(TgtCredentials credentials) {
        if (credentials == null || credentials.getTicket() == null) {
            throw new IllegalArgumentException("this argument is required; it must not be null");
        }

        // 根据TGT凭证获取用户身份信息
        UserAuthorization authorization = getCurrentAuthorization(credentials);
        if (authorization == null) {
            return null;
        }

        // TODO 校验IP，user-agent，refer是否一致

        return authorization;
    }


    /**
     * 为用户颁发一个用户访问指定应用的临时凭证（ST凭证）
     *
     * @param authorization 用户身份信息
     * @param app 要访问的应用
     * @return
     */
    @Override
    public ProvisionalCredentials issueProvisionalCredentials(UserAuthorization authorization, String app) {
        // 为用户颁发一个用户访问指定应用的临时凭证（ST凭证）
        String st = generator.generateProvisionalCredentials(authorization, app);
        ProvisionalCredentials provisionalCredentials = new ProvisionalCredentials(st, app);
        // 保存临时凭证
        storeProvisionalCertificate(provisionalCredentials, authorization);

        // 返回临时凭证
        return provisionalCredentials;
    }

    /**
     * 应用向SSO服务器验证用户提交的临时凭证（ST凭证），验证通过则返回一个代理凭证（PT凭证）
     *
     * @param credentials 临时凭证（ST凭证）
     * @return 用户身份信息
     */
    @Override
    public UserAuthorization validateSt(ProvisionalCredentials credentials) {
        // 如果临时凭证有效，根据临时凭证获取用户登录凭证
        UserAuthorization authorization = getCurrentAuthorization(credentials);
        if (authorization == null) {
            return null;
        }

        // 如果该应用已经被授权过了，清除原来的授权（或等其自己过期?）
        GrantedApplication granted = authorization.getApplication(credentials.getApp());
        if (granted != null) {
            // 从授权应用中去除
            authorization.removeApplication(credentials.getApp());
        }

        // 创建一个新的应用授权
        GrantedApplication app = new GrantedApplication(credentials.getApp(), credentials.getUrl());
        // 生成一个客户端应用授权票据
        app.setTikcet(generator.generatePt(authorization));
        // 加入授权应用
        authorization.addApplication(app);
        // 重新缓存
        storeCurrentAuthorization(authorization);

        return authorization;
    }

    @Override
    public void destoryTgt(TgtCredentials credentials) {
        // 根据TGT凭证获取用户身份信息
        UserAuthorization authorization = authenticate(credentials);
        if (authorization == null) {
            return;
        }

        // 通知应用登出
        if (authorization.getApplications() != null) {
            for (final GrantedApplication granted : authorization.getApplications()) {
                executorService.execute(() -> notifyApplication(granted, SSOServerContants.ACTION_LOGOUT));

            }
        }
        // 清除TGT
        removeCurrentAuthorization(authorization);
    }

    @Override
    public boolean accessiable(UserAuthorization authentication, String appname) {
        return true;
    }

    // certificates

    /**
     * 保存用户身份信息
     * @param authorization
     */
    protected abstract void storeCurrentAuthorization(UserAuthorization authorization);

    /**
     * 根据凭证获取用户身份信息
     * @param credentials
     * @return
     */
    protected abstract UserAuthorization getCurrentAuthorization(TgtCredentials credentials);

    /**
     * 保存临时登录凭证（ST凭证）
     */
    protected abstract void storeProvisionalCertificate(ProvisionalCredentials credentials, UserAuthorization authorization);

    /**
     * 验证临时登录凭证（ST凭证）
     * @return 身份信息
     */
    protected abstract UserAuthorization getCurrentAuthorization(ProvisionalCredentials credentials);

    /**
     * 清除用户身份信息
     * @param authorization
     */
    protected abstract void removeCurrentAuthorization(final UserAuthorization authorization);

    /**
     * 通知应用
     * @param application
     * @param action
     */
    protected abstract void notifyApplication(GrantedApplication application, String action);

    /**
     * 加密策略，你可以重置此方法以保持加密策略与注册时一致
     * @param password
     * @param slat
     * @return
     */
    protected String passwordEncode(String password, String slat) {
        if (slat == null) {
            return TextEncodeHelper.md5Encode(password);
        } else {
            return TextEncodeHelper.md5Encode(password, slat);
        }
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setGenerator(TicketGenerator generator) {
        this.generator = generator;
    }
}
