package com.yuanshenbin.developMode;

import java.io.Serializable;
import java.util.Map;

/**
 * author : yuanshenbin
 * time   : 2018/2/9
 * desc   :
 */

public class LogModel implements Serializable {


    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 请求地址
     */
    private String url;

    /**
     * 入参
     */
    private String param;

    /**
     * 出参
     */
    private String result;

    private long requestTime;

    /**
     * 链接质量
     */
    private String connectionQuality;

    /**
     * 每秒流量值
     */
    private double KBitsPerSecond;


    /**
     * 头部信息
     */
    private Map<String, String> headers;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public LogModel() {
    }

    public LogModel(String url, String param, String result) {
        this.url = url;
        this.param = param;
        this.result = result;
        this.createTime = System.currentTimeMillis();
    }

    public LogModel(String url, String param, String result, long requestTime) {
        this.url = url;
        this.param = param;
        this.result = result;
        this.requestTime = requestTime;
        this.createTime = System.currentTimeMillis();
    }

    public LogModel(String url, String param, String result, long requestTime, String connectionQuality, double KBitsPerSecond, Map<String, String> headers) {
        this.createTime = System.currentTimeMillis();
        this.url = url;
        this.param = param;
        this.result = result;
        this.requestTime = requestTime;
        this.connectionQuality = connectionQuality;
        this.KBitsPerSecond = KBitsPerSecond;
        this.headers = headers;
    }

    @Deprecated
    public LogModel(String url, String param, String result, long requestTime, String connectionQuality, double KBitsPerSecond) {
        this.createTime = System.currentTimeMillis();
        this.url = url;
        this.param = param;
        this.result = result;
        this.requestTime = requestTime;
        this.connectionQuality = connectionQuality;
        this.KBitsPerSecond = KBitsPerSecond;
    }

    public String getConnectionQuality() {
        return connectionQuality;
    }

    public void setConnectionQuality(String connectionQuality) {
        this.connectionQuality = connectionQuality;
    }

    public double getKBitsPerSecond() {
        return KBitsPerSecond;
    }

    public void setKBitsPerSecond(double KBitsPerSecond) {
        this.KBitsPerSecond = KBitsPerSecond;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "LogModel{" +
                "createTime=" + createTime +
                ", url='" + url + '\'' +
                ", param='" + param + '\'' +
                ", result='" + result + '\'' +
                ", requestTime=" + requestTime +
                ", connectionQuality='" + connectionQuality + '\'' +
                ", KBitsPerSecond='" + KBitsPerSecond + '\'' +
                '}';
    }
}
