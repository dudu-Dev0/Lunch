package com.dudu.watchface.example;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

;
import com.dudu.wearlauncher.model.WatchFace;
import java.io.IOException;
import java.util.Calendar;

/*
* 自定义表盘的切换速度视代码量而定，不要在build.gradle里引用库，它们无法被正常调用，只会增大包体和减慢切换速度
* 另外，也别开启minifyEnabled，一压缩就把你整个程序全缩没了
*/

public class WatchFaceImpl extends WatchFace {
    private static String path;
    private final Context context;
    private final Resources resources;

    private TextView tvBattery;
    private TextView tvStep;
    private TextView tvTime;
    private TextView tvDate;
    private TextView tvWeek;
    private ImageView ivStep;
    private ImageView ivBattery;
    private FrameLayout centerLayout;

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initView() {
        LayoutInflater.from(context).inflate(getResources().getLayout(R.layout.layout_main), this);

        tvBattery = findViewById(R.id.tv_battery);
        tvStep = findViewById(R.id.tv_step);
        tvTime = findViewById(R.id.tv_time);
        tvDate = findViewById(R.id.tv_date);
        tvWeek = findViewById(R.id.tv_week);
        ivBattery = findViewById(R.id.iv_battery);
        ivStep = findViewById(R.id.iv_step);
        centerLayout = findViewById(R.id.centerlayout);

        GifView gifView = new GifView(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(400, 200);
        layoutParams.gravity = Gravity.CENTER_VERTICAL+Gravity.END;
        layoutParams.bottomMargin = 220;
        
        gifView.setLayoutParams(layoutParams);
        
        try {
        	gifView.setGifStream(getResources().getAssets().open("astr.gif"));
        } catch(IOException err) {
        	err.printStackTrace();
        }
        
        centerLayout.addView(gifView);
        
        ivBattery.setBackground(getResources().getDrawable(R.drawable.battery));
        ivStep.setBackground(getResources().getDrawable(R.drawable.steps));
        tvTime.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/DS-DIGI-1.ttf"));
        tvStep.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/DS-DIGIB-2.ttf"));
        tvBattery.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/DS-DIGIB-2.ttf"));
        tvDate.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/DS-DIGIB-2.ttf"));
        tvWeek.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/DS-DIGIB-2.ttf"));
    }


    public void updateBattery(int i) {    //此函数在电池数值更新时执行，i是当前电量，i2是电池变红值
        tvBattery.setText(i+"%");
    }

    public void updateStep(int i) {
        tvStep.setText(i);
    }

    public void updateTime() {    //此函数在时间更新时执行
        int hour;
        Calendar calendar = Calendar.getInstance();
        if (DateFormat.is24HourFormat(context)) {
            hour = calendar.get(11);
        } else {
            hour = calendar.get(10);
            if (hour == 0) {
                hour = 12;
            }
        }
        int week = calendar.get(7);
        String minute = String.valueOf(calendar.get(12));
        String month = String.valueOf(calendar.get(2) + 1);
        String day = String.valueOf(calendar.get(5));
        Log.i("debug-dial", month + "月" + day + "日，星期" + week + "，" + hour + ":" + minute);

        if (minute.indexOf(0)!=0&&minute.length()!=2) minute = "0" + minute;
        if (month.indexOf(0)!=0&&month.length()!=2) month = "0" + month;
        if (day.indexOf(0)!=0&&month.length()!=2) day = "0" + day;

        tvTime.setText(hour+":"+minute);
        tvDate.setText(month+"-"+day);

        String weekStr = "";

        switch (week){
            case 1:weekStr="SUN";
                break;
            case 2:weekStr="MON";
                break;
            case 3:weekStr="TUE";
                break;
            case 4:weekStr="WED";
                break;
            case 5:weekStr="THU";
                break;
            case 6:weekStr="FRI";
                break;
            case 7:weekStr="SAT";
                break;
            default:
                break;
        }
        tvWeek.setText(weekStr);
    }
    public Resources initResources(Context context) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            assetManager.getClass().getMethod("addAssetPath", String.class).invoke(assetManager, path);
            Resources resources = context.getResources();
            return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public WatchFaceImpl(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.context = context;
        this.resources = initResources(context);
        initView();
    }

    public WatchFaceImpl(Context context) {
        this(context, null);
    }

    public WatchFaceImpl(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public static FrameLayout getWatchFace(Context context, String str) {
        path = str;
        return new WatchFaceImpl(context);
    }

    public Resources getResources() {
        return this.resources == null ? super.getResources() : this.resources;
    }

}