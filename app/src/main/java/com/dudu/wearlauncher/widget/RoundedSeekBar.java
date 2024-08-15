package com.dudu.wearlauncher.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dudu.wearlauncher.R;

public class RoundedSeekBar extends FrameLayout {
    Drawable icon;
    SeekBar seekBar;

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
        ((ImageView) findViewById(R.id.seek_bar_icon)).setImageDrawable(icon);
        seekBar = findViewById(R.id.seek_bar);

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
}
