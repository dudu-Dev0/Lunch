package com.dudu.wearlauncher.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dudu.wearlauncher.R;

import com.dudu.wearlauncher.model.App;
import com.dudu.wearlauncher.ui.BaseActivity;
import com.dudu.wearlauncher.utils.DensityUtil;
import com.dudu.wearlauncher.utils.ILog;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppListHolder> {

    Context context;
    List<App> appList;
    Map<String,Drawable> iconMap;
    String mode;
    public AppListAdapter(Context context, List<App> appList,Map<String,Drawable> iconMap,String mode) {
        this.context = context;
        this.appList = appList;
        this.iconMap = iconMap;
        this.mode = mode;
        
    }
    @Override
    public void onViewRecycled(AppListHolder holder){
        holder.appIcon.setImageBitmap(null);
    }
    @NonNull
    @Override
    public AppListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(mode.equals("linear")) {
        	view = LayoutInflater.from(this.context).inflate(R.layout.item_app_linear,parent,false);
        }if(mode.equals("grid")||mode.equals("bubble")) {
        	view = LayoutInflater.from(this.context).inflate(R.layout.item_app_grid,parent,false);
        }
        return new AppListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppListHolder holder, int position) {
        App activityInfo = appList.get(position);

        Drawable icon;
        if(iconMap.containsKey(activityInfo.packageName+"/"+activityInfo.activityName)) {
            icon = iconMap.get(activityInfo.packageName+"/"+activityInfo.activityName);
        }else{
            icon = activityInfo.icon;
        }
        //holder.appIcon.setImageBitmap(ImageUtils.toRound(ImageUtils.drawable2Bitmap(icon)));
        
        Glide.with(context).load(icon)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                //.skipMemoryCache(true)
                //.dontAnimate()
                //.dontTransform()
                .thumbnail(0.5f)
                .apply(RequestOptions.circleCropTransform())
                .override(DensityUtil.dip2px(context,48),DensityUtil.dip2px(context,48))
                .into(holder.appIcon);
        if(mode.equals("linear")) {
        	holder.appName.setText(activityInfo.label);
            holder.appName.setMaxWidth(DensityUtil.dip2px(context,90));
            holder.appSwitch.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClassName(activityInfo.packageName,activityInfo.activityName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
            } catch (Exception e){
                e.printStackTrace();
            }
        });/*
        holder.itemView.setOnLongClickListener(v->{
            Uri uri = Uri.fromParts("package", activityInfo.packageName, null);
            Intent intent = new Intent(Intent.ACTION_DELETE, uri);
            context.startActivity(intent);
            ((BaseActivity)context).overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            return false;
        });*/
    }
    public void addApp(App app) {
    	appList.add(app);
        notifyItemInserted(appList.size());
    }
    public void removeApp(App app) {
    	int position = appList.indexOf(app);
        appList.remove(position);
        notifyItemRemoved(position);
    }
    public List<App> getAppList() {
    	return appList;
    }
    public void removeApp(String packageName) {
        Iterator<App> iterator = appList.iterator();
        while(iterator.hasNext()) {
            App app = iterator.next();
        	if(app.packageName.equals(packageName)) {
                int position = appList.indexOf(app);
        		iterator.remove();
                notifyItemRemoved(position);
        	}
        }
    }
    @Override
    public int getItemCount() {
        return appList.size();
    }
    public void moveItem(int from,int to) {
    	App app = appList.get(from);
        appList.remove(from);
        appList.add(to,app);
        notifyItemMoved(from,to);
    }
    public static class AppListHolder extends RecyclerView.ViewHolder{
        ImageView appIcon;
        TextView appName;
        Switch appSwitch;

        public AppListHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.app_name);
            appIcon = itemView.findViewById(R.id.app_icon);
            appSwitch = itemView.findViewById(R.id.app_switch);
        }
    }
}