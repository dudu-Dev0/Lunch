package com.dudu.wearlauncher.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.Notification;
import com.dudu.wearlauncher.utils.ILog;
import com.dudu.wearlauncher.widget.FormattedTextClock;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.MsgListHolder> {
    Context context;
    List<Notification> msgList;

    public MsgListAdapter(Context context, List<Notification> msgList) {
        this.context = context;
        this.msgList = msgList;
    }

    @NotNull
    @Override
    public MsgListHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_msg, parent, false);
        return new MsgListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull MsgListHolder holder, int position) {
        Notification notification = msgList.get(position);
        Glide.with(context).load(notification.icon.loadDrawable(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions.circleCropTransform())
                //.override(DensityUtil.dip2px(context,10),DensityUtil.dip2px(context,10))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull @NotNull Drawable resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Drawable> transition) {
                        holder.msgImg.setForeground(resource);
                    }
                });
        holder.msgAppName.setText(notification.appName);
        holder.msgTime.setOriginalTime(new Date(notification.time));
        holder.msgTitle.setText(notification.title);
        holder.msgContent.setText(notification.content);
        holder.delBtn.setOnClickListener(v -> {
            Intent intent = new Intent("com.dudu.wearlauncher.NOTIFICATION_LISTENER");
            intent.putExtra("command", "cancelMsg");
            intent.putExtra("key", notification.key);
            context.sendBroadcast(intent);
        });
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public void addSbn(Notification sbn) {
        for (Notification notification : msgList) {
            if (notification.key.equals(sbn.key)) {
                Collections.replaceAll(msgList, notification, sbn);
                return;
            }
        }
        msgList.add(sbn);
        notifyItemInserted(getItemCount());
    }

    public void removeSbn(Notification sbn) {
        //new Thread(()->{
        while (true) {
            try {
                int pos = msgList.indexOf(sbn);
                if (pos == -1) {
                    break;
                }
                msgList.remove(sbn);
                notifyItemRemoved(pos);
            } catch (IndexOutOfBoundsException e) {
                ILog.e(e.getMessage() + "sbn is not available,skipped");
                break;
            }
        }
        //}).start();
    }

    public static class MsgListHolder extends RecyclerView.ViewHolder {
        ImageView msgImg;
        FormattedTextClock msgTime;
        TextView msgAppName, msgTitle, msgContent;
        ImageButton delBtn;
        public MsgListHolder(View itemView) {
            super(itemView);
            msgImg = itemView.findViewById(R.id.msg_icon);
            msgAppName = itemView.findViewById(R.id.msg_app_name);
            msgTime = itemView.findViewById(R.id.msg_time);
            msgTitle = itemView.findViewById(R.id.msg_title);
            msgContent = itemView.findViewById(R.id.msg_content);
            delBtn = itemView.findViewById(R.id.del_msg_btn);
        }
    }
}
