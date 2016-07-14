package io.summerx.framework.web.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Created by xiayg on 7/10/2016.
 */
public class WebUtils {

    public static Logger logger = LoggerFactory.getLogger(WebUtils.class);

    public static final String Question = "?";
    public static final String Equal = "=";
    public static final String Ampersand = "&";

    /**
     * 获得请求的IP
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况， 第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    public static String getRequestURLWithQueryString(HttpServletRequest request) {
        StringBuffer sBuf = new StringBuffer(request.getRequestURL());
        String query = request.getQueryString();

        if (query != null && query.length() > 0) {
            sBuf.append("?").append(query);
        }
        return sBuf.toString();
    }

    public static String getRequestURLWithQueryString(HttpServletRequest request, String... excludeParams) {
        if (excludeParams == null || excludeParams.length == 0) {
            return getRequestURLWithQueryString(request);
        }
        Map<String, Object> parameterMap = request.getParameterMap();
        if (parameterMap == null || !CollectionUtils.containsAny(parameterMap.keySet(), Arrays.asList(excludeParams))) {
            return getRequestURLWithQueryString(request);
        }

        // 拼接URL
        StringBuffer sBuf = new StringBuffer(request.getRequestURL());
        sBuf.append(Question);
        // 保留的参数
        Collection<String> paramNames = CollectionUtils.subtract(parameterMap.keySet(), Arrays.asList(excludeParams));
        // 拼接参数
        for (String paramName : paramNames) {
            String[] paramValues = request.getParameterValues(paramName);
            if (paramNames != null && paramValues.length == 1) {
                sBuf.append(paramName);
                sBuf.append(Equal);
                sBuf.append(paramValues[0]);
                sBuf.append(Ampersand);
            } else if (paramNames != null && paramValues.length > 1) {
                for (String value : paramValues) {
                    sBuf.append(paramName);
                    sBuf.append(Equal);
                    sBuf.append(value);
                    sBuf.append(Ampersand);
                }
            }
        }
        // 最后一个是&，删除它
        if (sBuf.toString().endsWith(Ampersand)) {
            sBuf.deleteCharAt(sBuf.length() - Ampersand.length());
        }
        return sBuf.toString();
    }
}
