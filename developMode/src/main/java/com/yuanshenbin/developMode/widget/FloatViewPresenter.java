package com.yuanshenbin.developMode.widget;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;


import com.yuanshenbin.developMode.DevelopMode;
import com.yuanshenbin.developMode.DevelopModeActivity;
import com.yuanshenbin.developMode.R;
import com.yuanshenbin.developMode.WindowManagerInstance;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/8/15.
 */

public class FloatViewPresenter implements FloatView.OnFloatViewClickListener {
    private WindowManager wm = null;
    private WindowManager.LayoutParams wmParams = null;
    private FloatView fsv = null;
    private Context mContext;
    public final static int REQUEST_CODE_OPEN_SETTINGS = 404;

    public void initFloatView(Context context) {
        this.mContext = context;
        if (isFloatWindowOpAllowed(context)) {
            wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
//        wmParams = new WindowManager.LayoutParams();
            wmParams = WindowManagerInstance.getInstance().getMywmParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            wmParams.format = PixelFormat.RGBA_8888;//设置图片格式，效果为背景透明

            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//
            wmParams.gravity = Gravity.LEFT | Gravity.TOP;//
            wmParams.width = (int) context.getResources().getDimension(R.dimen.develop_mode_float_width);
            wmParams.height = (int) context.getResources().getDimension(R.dimen.develop_mode_float_width);
            fsv = new FloatView(context);
            wmParams.x = fsv.sW / 2;
            wmParams.y = fsv.sH / 2;
            fsv.setOnFloatViewClickListener(this);
            fsv.setImageResource(DevelopMode.getInstance().getDevelopConfig().getWindowIcon());
            try {
                //只add一次
                if (!WindowManagerInstance.getInstance().hasView) {
                    Toast.makeText(context, "进入开发者模式，快速上滑浮动按钮退出", Toast.LENGTH_SHORT).show();
                    wm.addView(fsv, wmParams);
                    WindowManagerInstance.getInstance().hasView = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            showOpenSettingDialog(context);
        }

    }

    public void showOpenSettingDialog(final Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("悬浮窗权限管理")
                .setMessage("是否去开启悬浮窗权限？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //打开权限设置
                        openSetting(context);
                    }
                })
                .setNegativeButton("否", null)
                .create();
        dialog.show();


        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF4081"));
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#999999"));


    }

    private void openSetting(Context context) {
        Activity activity = (Activity) context;

        Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent1.setData(uri);
        activity.startActivityForResult(intent1, REQUEST_CODE_OPEN_SETTINGS);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, Context context) {
        if (requestCode == REQUEST_CODE_OPEN_SETTINGS) {
            initFloatView(context);
        }
    }


    /**
     * 判断悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean isFloatWindowOpAllowed(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return checkOp(context, 24);  // AppOpsManager.OP_SYSTEM_ALERT_WINDOW
        } else {
            return true;
        }
    }

    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class<?> spClazz = Class.forName(manager.getClass().getName());
                Method method = manager.getClass().getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int property = (Integer) method.invoke(manager, op,
                        Binder.getCallingUid(), context.getPackageName());
                Log.e("399", " property: " + property);

                if (AppOpsManager.MODE_ALLOWED == property) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e("399", "Below API 19 cannot invoke!");
        }
        return false;
    }

    @Override
    public void onFloatViewClick(Context context) {
        Intent intent = new Intent(context, DevelopModeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onFloatViewDisMiss(Context context) {
        hide();
    }

    public void hide() {
        if (fsv != null && wm != null) {
            wm.removeView(fsv);
            fsv = null;
            WindowManagerInstance.getInstance().hasView = false;
        }
    }

    public void show() {
        if (mContext != null) {
            initFloatView(mContext);
        }
    }

}
