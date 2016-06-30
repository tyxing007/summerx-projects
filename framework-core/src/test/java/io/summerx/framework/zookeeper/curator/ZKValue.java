package io.summerx.framework.zookeeper.curator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xiayg on 6/30/2016.
 */
public class ZKValue {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String url;

    private String username;

    private String password;

    public ZKValue() {
        logger.info("Craete ZKValue");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
