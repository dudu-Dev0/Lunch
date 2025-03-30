package com.dudu.wearlauncher.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {
    public final static String TAG = "ClickTestActivity";
    private static int LONG_CLICK_TIME = 600;
    Activity mActivity;
    int mTouchSlop; //最短滑动距离
    //记录按下时的坐标
    int downX = 0;
    int downY = 0;
    private boolean isLongClick = false; //是否是长点击事件
    private boolean isRelease = false; //是否已经释放
    private LongClickListener longClickListener;
    private Runnable countDownRunnable = new Runnable() {
        @Override
        public void run() {
            isLongClick = true;
            //当用户在LONG_CLICK_TIME时间内没有做抬起滑动等取消动作，则触发longclick事件
            if (!isRelease) {
                return;
            }
            isRelease = true;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (longClickListener != null) {
                        longClickListener.OnLongClick();
                    }
                }
            });
        }
    };

    public MyFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mActivity = (Activity) context;
        mTouchSlop = ViewConfiguration.get(mActivity).getScaledTouchSlop();
    }

    public void setLongClickListener(LongClickListener l) {
        this.longClickListener = l;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                isRelease = false;
                isLongClick = false;
                //延迟LONG_CLICK_TIME毫秒的时间，触发长点击事件
                postDelayed(countDownRunnable, LONG_CLICK_TIME);
                break;
            case MotionEvent.ACTION_MOVE:
                //当横移或纵移的长度大于系统规定的滑动最短距离时，则视为用户取消了longclick事件
                if (Math.abs(event.getX() - downX) > mTouchSlop || Math.abs(event.getY() - downY) > mTouchSlop || isRelease) {
                    removeCallbacks(countDownRunnable);
                    return true;
                }
            case MotionEvent.ACTION_UP:
                isRelease = true;
                removeCallbacks(countDownRunnable);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                isRelease = true;
                if (isLongClick) {
                    //当已经是longclick事件时，parent则拦截该事件，child不会再收到该事件
                    return true;
                }
                break;
        }

        boolean isDispatch = super.dispatchTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isDispatch) {
            //当down事件返回false时 不触发up事件 所以返回true强制触发UP事件，否则会出现click父布局出现longclick的效果
            return true;
        }
        return isDispatch;
    }

    //自定义长点击事件接口
    public interface LongClickListener {
        void OnLongClick();
    }
}
