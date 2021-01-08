package com.yuanshenbin.developMode;

/**
 * Created by Administrator on 2017/4/20.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjian
 * 日志记录到本地的思路
 * 1.market版本不记录任何日志
 * 2.先建立一个log文件夹，用来存储log文件
 * 3.新建一个log文件，定义为yijiupi.log，用来存储log
 * 4.最多存储30组日志，一组日志分为request，params和response
 */
public class LogRecorder {
    public static LogRecorder instance;
    private List<LogModel> logModels = new ArrayList<>();

    public static synchronized LogRecorder getInstance(){
        if (instance == null){
            instance = new LogRecorder();
        }
        return instance;
    }

    public void addLog(LogModel logModel){
        if (logModels.size()>30){
            logModels.remove(0);
        }
        logModels.add(logModel);
    }


    public List<LogModel> getLogModels() {
        return logModels;
    }

    public void setLogModels(List<LogModel> logModels) {
        this.logModels = logModels;
    }
}
