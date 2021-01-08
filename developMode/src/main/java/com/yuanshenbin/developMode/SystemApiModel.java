package com.yuanshenbin.developMode;

import java.io.Serializable;
import java.util.Arrays;

/**
 * author : yuanshenbin
 * time   : 2018/8/29
 * desc   : 系统api模型
 */
public class SystemApiModel implements Serializable {

    private String[] apiUrl;//api地址
    private boolean checked;
    private int appCode;//code
    private String appVersion;//版本号
    private String ambient[];//环境
    private int type;//类型


    public SystemApiModel() {
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getAmbient() {
        return ambient;
    }

    public void setAmbient(String... ambient) {
        this.ambient = ambient;
    }

    public String[] getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String... apiUrl) {
        this.apiUrl = apiUrl;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getAppCode() {
        return appCode;
    }

    public void setAppCode(int appCode) {
        this.appCode = appCode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
        return "SystemApiModel{" +
                "apiUrl=" + Arrays.toString(apiUrl) +
                ", checked=" + checked +
                ", appCode='" + appCode + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", ambient=" + Arrays.toString(ambient) +
                '}';
    }
}
