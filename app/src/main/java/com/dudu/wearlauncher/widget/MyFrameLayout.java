package com.dudu.wearlauncher.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {
    private static int LONG_CLICK_TIME = 600;
    Activity mActivity;
    LongClickListener longClickListener = new LongClickListener(){
        @Override
        public void OnLongClick() {
        }
        
    };

    private Runnable longPressRunnable =
            new Runnable() {
                @Override
                public void run() {
                    hasPerformedLongPress = true;
                    longClickListener.OnLongClick();
                }
            };

    private long touchDownTime;
    private boolean hasPerformedLongPress;
    private float startX,startY;
    

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
    }

    public void setLongClickListener(LongClickListener l) {
        this.longClickListener = l;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownTime = System.currentTimeMillis();
                hasPerformedLongPress = false;
                startX = event.getX();
                startY = event.getY();
                postDelayed(longPressRunnable, LONG_CLICK_TIME);
                break;

            case MotionEvent.ACTION_MOVE:
                if (isMoveExceededSlop(event)) {
                    removeCallbacks(longPressRunnable);
                }
                break;

            case MotionEvent.ACTION_UP:
                removeCallbacks(longPressRunnable);
                if (!hasPerformedLongPress) {
                    performClick();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                removeCallbacks(longPressRunnable);
                break;
        }
        return true;
    }

    private boolean isMoveExceededSlop(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float dx = Math.abs(x - startX);
        float dy = Math.abs(y - startY);
        int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        return (dx > touchSlop) || (dy > touchSlop);
    }
    public interface LongClickListener {
        void OnLongClick();
    }
}
