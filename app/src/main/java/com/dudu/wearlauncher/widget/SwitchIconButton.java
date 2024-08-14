package com.dudu.wearlauncher.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import com.dudu.wearlauncher.R;
import org.jetbrains.annotations.NotNull;

public class SwitchIconButton extends AppCompatImageButton {
    public SwitchIconButton(@NonNull @NotNull Context context) {
        this(context, null);
    }

    public SwitchIconButton(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchIconButton(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setActivated(false);
        setForeground(getResources().getDrawable(R.drawable.ripple_effect));
        setBackground(getResources().getDrawable(R.drawable.circle_bg_gray));
        setOnClickListener(v -> {
            //Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            //setActivated(!isActivated());
        });
        setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                super.performClick();
                setActivated(!isActivated());
            }
            return false;
        });
        setScaleType(ScaleType.CENTER);
    }

}
