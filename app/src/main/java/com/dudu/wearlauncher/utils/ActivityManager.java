package com.dudu.wearlauncher.utils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Transformation;
import androidx.core.app.ActivityOptionsCompat;
import com.dudu.wearlauncher.ui.AppTransformActivity;
import com.dudu.wearlauncher.ui.BaseActivity;
import com.google.android.material.transition.MaterialContainerTransform;

public class ActivityManager {
    public static void startActivityWithTransAnimation(BaseActivity activity,String packageName,String activityName,View view,Drawable image) {
        Transition trans = activity.getWindow().getSharedElementEnterTransition();
        if(trans!=null) {
        	activity.getWindow().getSharedElementEnterTransition().setInterpolator(new AccelerateDecelerateInterpolator());
            activity.getWindow().getSharedElementEnterTransition().setDuration(1000);
        }
        Intent intent = new Intent(activity,AppTransformActivity.class);
        intent.putExtra("image",ImageUtil.drawableToBitmap(image,256,256));
        intent.putExtra("packageName",packageName);
        intent.putExtra("activityName",activityName);
        try {
            Bundle animateBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,view,"app_shared_view").toBundle();
        	activity.startActivity(intent,animateBundle);
        } catch(Exception err) {
            err.printStackTrace();
        	activity.startActivity(intent);
        }
        
        if(trans!=null) {
            activity.getWindow().setSharedElementEnterTransition(trans);
        }
    }
}
