package io.summerx.framework.zookeeper;

import java.util.concurrent.TimeUnit;

/**
 * ZooKeeper配置，用以连接到ZK服务器
 */
public class ZookeeperProperties {

    /**
     * 编码
     */
    private String charset = "UTF-8";

    /**
     * ZooKeeper连接串
     */
    private String connectString = "localhost:2181";

    /**
     * 连接超时时间
     */
    private int connectionTimeoutMs = 5000;

    /**
     * 会话超时时间
     */
    private int sessionTimeoutMs = 5000;

    /**
     * 最大重试次数
     */
    private Integer maxRetries = 10;

    /**
     * 初始重试时间间隔
     */
    private Integer baseSleepMs = 500;

    /**
     * 最大重试时间间隔
     */
    private Integer maxSleepMs = 500;

    /**
     * 等待连接到ZooKeeper的时间
     */
    private Integer blockUntilConnectedWait = 10;
    /**
     * 等待连接到ZooKeeper的时间单位
     */
    private TimeUnit blockUntilConnectedUnit = TimeUnit.SECONDS;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getBaseSleepMs() {
        return baseSleepMs;
    }

    public void setBaseSleepMs(Integer baseSleepMs) {
        this.baseSleepMs = baseSleepMs;
    }

    public Integer getMaxSleepMs() {
        return maxSleepMs;
    }

    public void setMaxSleepMs(Integer maxSleepMs) {
        this.maxSleepMs = maxSleepMs;
    }

    public Integer getBlockUntilConnectedWait() {
        return blockUntilConnectedWait;
    }

    public void setBlockUntilConnectedWait(Integer blockUntilConnectedWait) {
        this.blockUntilConnectedWait = blockUntilConnectedWait;
    }

    public TimeUnit getBlockUntilConnectedUnit() {
        return blockUntilConnectedUnit;
    }

    public void setBlockUntilConnectedUnit(TimeUnit blockUntilConnectedUnit) {
        this.blockUntilConnectedUnit = blockUntilConnectedUnit;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
