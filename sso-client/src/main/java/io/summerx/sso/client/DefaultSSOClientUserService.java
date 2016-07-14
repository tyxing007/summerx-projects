package io.summerx.sso.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.summerx.framework.utils.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 客户端（业务应用）通过ST和应用名称向SSO服务器验证ST有效并获取用户授权的信息
 *
 * @author summerx
 * @Date 2016-07-14 3:02 PM
 */
public class DefaultSSOClientUserService implements SSOClientUserService {

    // 客户端（业务应用）的标识
    private String appname;

    // 客户端（业务应用）验证ST并获取用户信息的URL，该地址由SSO服务器提供的（内网访问地址）
    private String ssoUserUrl;

    public DefaultSSOClientUserService() {
    }

    public DefaultSSOClientUserService(String appname, String ssoUserUrl) {
        this.appname = appname;
        this.ssoUserUrl = ssoUserUrl;
    }

    /**
     * 客户端（业务应用）通过ST和应用名称向SSO服务器验证ST有效并获取用户授权的信息
     *
     * @param st
     * @return
     */
    public UserObject getAuthorizationUser(String st) {
        RestTemplate rt = new RestTemplate();
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("st", st);
        requestEntity.add("app", appname);
        String response = rt.postForObject(getSsoUserUrl(), requestEntity, String.class);
        if (!StringUtils.isEmpty(response)) {
            JSONObject jsonObject = JSON.parseObject(response);
            if (!StringUtils.isEmpty(jsonObject.getString("errtx"))) {
                return null;
            }
            if (StringUtils.isEmpty(jsonObject.getString("username"))) {
                return null;
            }

            UserObject userObject = new UserObject();
            userObject.setUsername(jsonObject.getString("username"));
            return userObject;
        }
        return null;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getSsoUserUrl() {
        return ssoUserUrl;
    }

    public void setSsoUserUrl(String ssoUserUrl) {
        this.ssoUserUrl = ssoUserUrl;
    }
}
