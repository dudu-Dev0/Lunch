package com.dudu.wearlauncher.ui;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import com.blankj.utilcode.util.ImageUtils;
import com.dudu.wearlauncher.R;

public class AppTransformActivity extends BaseActivity{
    Handler handler = new Handler(Looper.getMainLooper());
    String packageName;
    String activityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_transform);
        Intent data = getIntent();
        Bitmap image = (Bitmap)data.getParcelableExtra("image");
        packageName = data.getStringExtra("packageName");
        activityName = data.getStringExtra("activityName");
        ((ImageView)findViewById(R.id.icon)).setImageBitmap(ImageUtils.toRound(image));
        handler.postDelayed(this::startActivity,1000);
    }
    private void startActivity() {
        Intent intent = new Intent();
        intent.setClassName(packageName,activityName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = ActivityOptions.makeCustomAnimation(this,R.anim.fade_in,R.anim.fade_out).toBundle();
        try {
            startActivity(intent,bundle);
            handler.postDelayed(this::finish,500);
        } catch (Exception e){
            startActivity(intent);
            e.printStackTrace();
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
    
}
