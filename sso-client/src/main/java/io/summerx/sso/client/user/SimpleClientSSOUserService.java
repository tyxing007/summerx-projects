package io.summerx.sso.client.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.summerx.framework.utils.RandomUtils;
import io.summerx.framework.utils.StringUtils;
import io.summerx.sso.client.config.SSOClientConfig;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 客户端（业务应用）通过ST和应用名称向SSO服务器验证ST有效并获取用户授权的信息
 *
 * @author summerx
 * @Date 2016-07-14 3:02 PM
 */
public class SimpleClientSSOUserService implements ClientSSOUserService {

    /**
     * SSO客户端配置
     */
    private SSOClientConfig ssoClientConfig;

    // 客户端（业务应用）的标识
    private String appname;

    public SimpleClientSSOUserService(String appname, SSOClientConfig ssoClientConfig) {
        this.appname = appname;
        this.ssoClientConfig = ssoClientConfig;
    }

    /**
     * 客户端（业务应用）通过ST和应用名称向SSO服务器验证ST有效并获取用户授权的信息
     *
     * @param st
     * @return
     */
    public ClientSSOUser getAuthorizationUser(String st) {
        RestTemplate rt = new RestTemplate();
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        // SSO服务器颁发的ST
        requestEntity.add("st", st);
        // 请求授权的应用
        requestEntity.add("app", appname);
        // 回调地址
        requestEntity.add("url", ssoClientConfig.getCallbackUrl());
        // 附上一个随机数防止缓存
        requestEntity.add("rnd", RandomUtils.generateAlphaNumberSequence(8));
        String response = rt.postForObject(ssoClientConfig.getUserUrl(), requestEntity, String.class);
        if (!StringUtils.isEmpty(response)) {
            JSONObject jsonObject = JSON.parseObject(response);
            // 出错了?
            if (!StringUtils.isEmpty(jsonObject.getString("errtx"))) {
                return null;
            }

            ClientSSOUser userDetail = new ClientSSOUser();
            userDetail.setUsername(jsonObject.getString("username"));
            userDetail.setTicket(jsonObject.getString("ticket"));
            if (StringUtils.isEmpty(userDetail.getUsername()) || StringUtils.isEmpty(userDetail.getTicket())) {
                return null;
            }

            return extraAuthorizationUser(userDetail);
        }
        return null;
    }

    public ClientSSOUser extraAuthorizationUser(ClientSSOUser userDetail) {
        return userDetail;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public void setSsoClientConfig(SSOClientConfig ssoClientConfig) {
        this.ssoClientConfig = ssoClientConfig;
    }
}
