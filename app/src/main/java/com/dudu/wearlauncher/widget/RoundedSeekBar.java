package com.dudu.wearlauncher.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dudu.wearlauncher.R;

public class RoundedSeekBar extends FrameLayout {
    Drawable icon;
    SeekBar seekBar;
    ImageView iconView;
    public RoundedSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RoundedSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedSeekBar);
        icon = typedArray.getDrawable(R.styleable.RoundedSeekBar_left_icon);
        LayoutInflater.from(context).inflate(R.layout.widget_seek_bar, this);
        seekBar = findViewById(R.id.seek_bar);
        iconView = findViewById(R.id.seek_bar_icon);
        iconView.setImageDrawable(icon);
        /*setOnTouchListener((v, event) -> {
            ViewGroup parent = (ViewGroup)v.getParent();
            while(true) {
            	if(parent instanceof MyViewPager) {
            		break;
            	}
                parent = (ViewGroup)parent.getParent();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    parent.requestDisallowInterceptTouchEvent(true);
                    ((MyViewPager)parent).setScrollble(true);
                    break;
                case MotionEvent.ACTION_UP:
                    parent.requestDisallowInterceptTouchEvent(false);
                    ((MyViewPager)parent).setScrollble(false);
                    break;
            }
            return false;
        });
        */
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ViewGroup parent = (ViewGroup)getParent();
        while(true) {
        	if(parent instanceof MyViewPager) {
            	break;
        	}
            parent = (ViewGroup)parent.getParent();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parent.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                parent.requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    
    public int getProgress() {
        return seekBar.getProgress();
    }

    public void setProgress(int progress) {
        seekBar.setProgress(progress);
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener) {
        seekBar.setOnSeekBarChangeListener(listener);
    }

    public void setIconDrawable(Drawable icon){
        this.icon = icon;
        iconView.setImageDrawable(icon);
    }
    public void setIconOnClickListener(OnClickListener listener) {
    	iconView.setOnClickListener(listener);
    }
}
