package com.dudu.wearlauncher.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.Notification;
import com.dudu.wearlauncher.utils.ILog;
import com.dudu.wearlauncher.widget.FormattedTextClock;
import org.jetbrains.annotations.NotNull;

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
        Glide.with(context).load(notification)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.msgImg);
        holder.msgAppName.setText(notification.appName);
        //holder.msgTime.setOriginalTime(new Date(sbn.getPostTime()));
        holder.msgTitle.setText(notification.title);
        holder.msgContent.setText(notification.content);
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public void addSbn(Notification sbn) {
        msgList.add(sbn);
        notifyItemInserted(getItemCount());
    }

    public void removeSbn(Notification sbn) {
        try {
            int pos = msgList.indexOf(sbn);
            msgList.remove(sbn);
            notifyItemRemoved(pos);
        } catch (IndexOutOfBoundsException e) {
            ILog.e(e.getMessage() + "sbn is not available,skipped");
        }
    }

    public static class MsgListHolder extends RecyclerView.ViewHolder {
        ImageView msgImg;
        FormattedTextClock msgTime;
        TextView msgAppName, msgTitle, msgContent;
        public MsgListHolder(View itemView) {
            super(itemView);
            msgImg = itemView.findViewById(R.id.msg_icon);
            msgAppName = itemView.findViewById(R.id.msg_app_name);
            msgTime = itemView.findViewById(R.id.msg_time);
            msgTitle = itemView.findViewById(R.id.msg_title);
            msgContent = itemView.findViewById(R.id.msg_content);
        }
    }
}
