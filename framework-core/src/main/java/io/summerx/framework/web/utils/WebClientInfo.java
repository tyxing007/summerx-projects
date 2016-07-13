package io.summerx.framework.web.utils;

import javax.servlet.http.HttpServletRequest;

public class WebClientInfo implements java.io.Serializable {

    private String ip;

    private String protocol;

    private String userAgent;

    private String refer;

    public WebClientInfo(HttpServletRequest request) {
        this.ip = WebUtils.getIpAddress(request);
        this.protocol = request.getProtocol();
        this.userAgent = request.getHeader("user-agent");
        this.refer = request.getHeader("Referer");
    }

    public String getIp() {
        return ip;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getRefer() {
        return refer;
    }
}
