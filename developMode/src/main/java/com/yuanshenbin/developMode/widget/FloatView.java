package com.yuanshenbin.developMode.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.yuanshenbin.developMode.WindowManagerInstance;


/**
 * Created by kitchee on 2017/1/10.
 * Description：全局悬浮窗口
 */

public class FloatView extends AppCompatImageView {


    private final int statusHeight;
    int sW;
    int sH;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private boolean isMove = false;
    private Context context;

    private WindowManager wm = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    //此wmParams变量为获取的全局变量，用以保存悬浮窗口的属性
    private WindowManager.LayoutParams wmParams = WindowManagerInstance.getInstance().getMywmParams();
    private float mLastX;
    private float mLastY;
    private float mStartX;
    private float mStartY;
    private long mLastTime;
    private long mCurrentTime;


    public FloatView(Context context) {
        this(context, null);
        this.context = context;
    }

    public FloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sW = wm.getDefaultDisplay().getWidth();
        sH = wm.getDefaultDisplay().getHeight();
        statusHeight = getStatusHeight(context);

    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY() - statusHeight;   //statusHeight是系统状态栏的高度
        Log.i("currP", "currX" + x + "====currY" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:    //捕获手指触摸按下动作
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                mLastTime = System.currentTimeMillis();
                Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                isMove = false;
                break;

            case MotionEvent.ACTION_MOVE:   //捕获手指触摸移动动作
                updateViewPosition();
                isMove = true;
                mLastY = event.getRawY();
                if (mLastY <= 100) {
                    //处理点击的事件
                    if (listener != null) {
                        listener.onFloatViewDisMiss(getContext());
                    }
                }
                break;

            case MotionEvent.ACTION_UP:    //捕获手指触摸离开动作
                mLastX = event.getRawX();
                mLastY = event.getRawY();

                // 抬起手指时让floatView紧贴屏幕左右边缘
                /*wmParams.x = wmParams.x <= (sW / 2) ? 0 : sW;
                wmParams.y = (int) (y - mTouchStartY);
                wm.updateViewLayout(this, wmParams);*/


                mCurrentTime = System.currentTimeMillis();
                if (mCurrentTime - mLastTime < 800) {
                    if (Math.abs(mStartX - mLastX) < 10.0 && Math.abs(mStartY - mLastY) < 10.0) {
                        //处理点击的事件
                        if (listener != null) {
                            listener.onFloatViewClick(getContext());
                        }
                    }
                }

                break;
        }
        return true;
    }


    private void updateViewPosition() {
        //更新浮动窗口位置参数
        wmParams.x = (int) (x - mTouchStartX);
        wmParams.y = (int) (y - mTouchStartY);
        wm.updateViewLayout(this, wmParams);  //刷新显示

    }

    private OnFloatViewClickListener listener;

    public void setOnFloatViewClickListener(OnFloatViewClickListener listener) {
        this.listener = listener;
    }

    public interface OnFloatViewClickListener {
        void onFloatViewClick(Context context);

        void onFloatViewDisMiss(Context context);
    }
}
