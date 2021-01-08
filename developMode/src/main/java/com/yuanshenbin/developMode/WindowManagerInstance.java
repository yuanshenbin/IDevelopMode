package com.yuanshenbin.developMode;

import android.view.WindowManager;

/**
 * Created by Administrator on 2017/8/15.
 */

public class WindowManagerInstance {

    public static WindowManagerInstance instance;
    public WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
    public  boolean hasView = false;

    public WindowManager.LayoutParams getMywmParams(){
        return wmParams;
    }

    public static synchronized WindowManagerInstance getInstance(){
        if (instance == null){
            instance = new WindowManagerInstance();
        }
        return instance;
    }
}
