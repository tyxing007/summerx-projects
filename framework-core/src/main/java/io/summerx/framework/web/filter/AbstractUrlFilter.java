package io.summerx.framework.web.filter;

import io.summerx.framework.utils.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 扩展web.xml的url-pattern，支持Ant风格的通配符
 *
 * @author summerx
 * @Date 2016-07-13
 */
public abstract class AbstractUrlFilter implements Filter {

    /**
     * 默认false，表示配置的Url需要过滤，其他不需要
     * true则表示除了配置的Url，其他都需要过滤
     */
    private boolean exclude = false;

    /**
     * 多个Url Pattern时的分隔符
     */
    private String delimiters = ",\n;";

    /**
     * 路径匹配器
     */
    private PathMatcher pathMatcher = new AntPathMatcher();

    /**
     * url-pattern
     */
    private Set<String> patterns = new HashSet<String>();

    @Override
    public void init(FilterConfig fc) throws ServletException {
        if (fc == null) {
            return;
        }
        // 初始化Url Pattern
        String strPattern = fc.getInitParameter("pattern");
        if (!StringUtils.isEmpty(strPattern)) {
            String[] patternArray = StringUtils.split(strPattern.trim(), this.delimiters);
            for (String pattern : patternArray) {
                if (!StringUtils.isEmpty(pattern)) {
                    this.patterns.add(pattern);
                }
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 只支持HTTP资源
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isFilter(((HttpServletRequest) request).getServletPath())) {
            doFilterInternal((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    protected abstract void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException;

    /**
     * 是否需要过滤
     *
     * @param requestUrl
     * @return 如果返回true，表示不需要对给定的url进行的过滤。
     */
    protected boolean isFilter(String requestUrl) {
        for (String pattern : this.patterns) {
            if (getPathMatcher().match(pattern, requestUrl)) {
                return !exclude;
            }
        }

        return exclude;
    }

    public void setExclude(boolean exclude) {
        this.exclude = exclude;
    }

    public PathMatcher getPathMatcher() {
        if (this.pathMatcher == null) {
            this.pathMatcher = new AntPathMatcher();
        }
        return this.pathMatcher;
    }

    public void setPatterns(Set<String> patterns) {
        this.patterns = patterns;
    }

    @Override
    public void destroy() {
    }
}