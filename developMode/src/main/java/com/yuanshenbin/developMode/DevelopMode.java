package com.yuanshenbin.developMode;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.yuanshenbin.developMode.util.FileUtils;
import com.yuanshenbin.developMode.widget.FloatViewPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author : yuanshenbin
 * time   : 2018/8/29
 * desc   :
 */

public class DevelopMode {
    public final static String SPLIT_TAG = "#111111";
    private int clickCount = 0;
    private static DevelopMode instance;

    private final static int DEVELOP_MODE_STEP_COUNT = 10;
    private long lastTime = 0;

    private List<Activity> mStacks = new ArrayList<>();


    private DevelopConfig mDevelopConfig;

    public DevelopConfig getDevelopConfig() {
        return mDevelopConfig;
    }

    public List<SystemApiModel> getApiModels() {
        for (int i = 0; i < mApiModels.size(); i++) {
            mApiModels.get(i).setChecked(false);
        }
        return mApiModels;
    }

    private List<SystemApiModel> mApiModels = new ArrayList<>();

    public void add(Activity activity) {
        mStacks.add(activity);
    }

    public void remove(Activity activity) {
        mStacks.remove(activity);
    }

    public void clear() {
        for (Activity stack : mStacks) {
            stack.finish();
        }
    }


    public static synchronized DevelopMode getInstance() {
        if (instance == null)
            synchronized (DevelopMode.class) {
                instance = new DevelopMode();
            }
        return instance;
    }

    public void InitializationConfig(DevelopConfig config) {
        this.mDevelopConfig = config;
        if (config != null && !config.isDebug()) {
            ExceptionHandler.getInstance().init(config.getContext().getApplicationContext(),config.getContext().getApplicationContext().getPackageName() + ".logErr.txt");
        }
        mApiModels.clear();
        mApiModels.addAll(config.getApiModels());
    }


    /**
     * 进入开发者模式
     * 需要传入一个view。
     * 通过这个view的长按事件来触发开发者模式
     * 模仿android系统进入开发者模式的方法进行
     */
    public void setUpDevelopMode(View view, final OnSystemApiSwitchListener listener) {
        if (mDevelopConfig == null) {
            throw new ExceptionInInitializerError("Please invoke deevelopConfig~");
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCount++;
                if (clickCount == 1) {
                    lastTime = System.currentTimeMillis();
                }
                if (clickCount >= DEVELOP_MODE_STEP_COUNT) {
                    clickCount = 0;
                    if (System.currentTimeMillis() - lastTime <= 5000) {
                        setUpDevelopMode(view.getContext(), listener);
                    }
                    lastTime = 0;
                }
            }
        });

    }

    private static FloatViewPresenter presenter;
    private OnSystemApiSwitchListener mListener;

    public OnSystemApiSwitchListener getListener() {
        return mListener;
    }

    /**
     * 同时开放一个无参数的api给业务使用，
     * 可能前端会使用不同的方式来进行调用而进入开发者模式
     */
    public void setUpDevelopMode(Context context, OnSystemApiSwitchListener listener) {
        mListener = listener;
        if (mDevelopConfig.getMode() == DevelopConfig.Mode.WINDOW) {
            if (presenter == null) {
                presenter = new FloatViewPresenter();
            }
            presenter.initFloatView(context);
        } else if (mDevelopConfig.getMode() == DevelopConfig.Mode.VIEW) {
            WindowManagerInstance.getInstance().hasView = true;
            context.startActivity(new Intent(context, DevelopModeActivity.class));

        } else if (mDevelopConfig.getMode() == DevelopConfig.Mode.NOTICE) {
            WindowManagerInstance.getInstance().hasView = true;
            showNotify(context);
        }
    }


    private static NotificationManager mNotificationManager;

    private static Notification mNotification;

    /**
     * 显示常驻通知栏
     */
    private void showNotify(Context context) {

        if (mNotificationManager == null || mNotification == null) {
            NotificationCompat.Builder mBuilder = null;
            String PUSH_CHANNEL_ID = String.valueOf(context.getPackageName().hashCode());
            String PUSH_CHANNEL_NAME = getAppName(context);
            mBuilder = new NotificationCompat.Builder(context, PUSH_CHANNEL_ID);

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(channel);
                }
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, DevelopModeActivity.class), 0);
            mBuilder.setSmallIcon(R.drawable.develop_mode_ic_launcher_round)
                    .setTicker("开发者权限已打开- -！")
                    .setContentTitle(PUSH_CHANNEL_NAME + " → 开发者调试常驻")
//                .setContentText("打一谜底：是先有鸡还是先有蛋")
                    .setContentIntent(pendingIntent);
            mNotification = mBuilder.build();
            //设置通知  消息  图标
            mNotification.icon = DevelopMode.getInstance().getDevelopConfig().getWindowIcon();
            //在通知栏上点击此通知后自动清除此通知
            mNotification.flags |= Notification.FLAG_ONGOING_EVENT;//FLAG_ONGOING_EVENT 在顶部常驻，可以调用下面的清除方法去除  FLAG_AUTO_CANCEL  点击和清理可以去调
            //设置显示通知时的默认的发声、震动、Light效果
            mNotification.defaults = Notification.DEFAULT_VIBRATE;
            //设置发出消息的内容
            mNotification.tickerText = "开发者";
            //设置发出通知的时间
            mNotification.when = System.currentTimeMillis();

            mNotificationManager.notify(0, mNotification);
        } else {

            mNotificationManager.notify(0, mNotification);
        }


    }

    public void addLog(LogModel logModel) {
        if (WindowManagerInstance.getInstance().hasView) {
            LogRecorder.getInstance().addLog(logModel);
        }

    }


    public List<LogModel> getLogModels() {
        return LogRecorder.getInstance().getLogModels();
    }

    public void setLogModels(List<LogModel> logModels) {
        LogRecorder.getInstance().setLogModels(logModels);
    }

    private static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "开发者模式";

    }


    public void shareFile(Context context) {
        FileUtils.shareFile(context, new File(ExceptionHandler.getInstance().path + ExceptionHandler.getInstance().fileName));
    }

    public File getErrorFile() {
        return new File(ExceptionHandler.getInstance().path + ExceptionHandler.getInstance().fileName);
    }

//    /**
//     * 分享轨迹
//     *
//     * @param context
//     */
//    public void shareTrajectory(Context context) {
//        FileUtils.shareFile(context, new File(ExceptionHandler.getInstance().path + ExceptionHandler.getInstance().fileNameTrajectory));
//    }
//
//    /**
//     * 记录轨迹
//     *
//     * @param json
//     */
//    public void recordTrajectory(String json) {
//        ExceptionHandler.getInstance().saveCrashInfo2File1(json);
//    }
//
//    /**
//     * 删除轨迹
//     */
//    public void deleteTrajectory() {
//        File file = new File(ExceptionHandler.getInstance().path + ExceptionHandler.getInstance().fileNameTrajectory);
//        if (file.exists()) {
//            file.delete();
//        }
//    }
}
