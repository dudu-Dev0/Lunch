package com.dudu.wearlauncher.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {

    private boolean scrollble = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if(!scrollble) return true;
        }
        return super.onTouchEvent(ev);
    }
    
    private float mInitialX;
    private float mInitialY;
    private static final int SLOP = 20; // 滑动阈值，单位像素

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialX = ev.getX();
                mInitialY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - mInitialX;
                float dy = ev.getY() - mInitialY;

                float absDx = Math.abs(dx);
                float absDy = Math.abs(dy);
                
                if (absDx > SLOP && absDx > absDy) {
                    if(!scrollble) return true; // 拦截左右滑动事件
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}