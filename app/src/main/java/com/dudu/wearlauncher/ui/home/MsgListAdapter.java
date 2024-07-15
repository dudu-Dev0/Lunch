package com.dudu.wearlauncher.ui.home;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.dudu.wearlauncher.R;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.MsgListHolder> {
    Context context;
    List<StatusBarNotification> msgList;

    public MsgListAdapter(Context context, List<StatusBarNotification> msgList) {
        this.context = context;
        this.msgList = msgList;
    }

    @NotNull
    @Override
    public MsgListHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_app, parent, false);
        return new MsgListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull MsgListHolder holder, int position) {
        StatusBarNotification sbn = msgList.get(position);
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public static class MsgListHolder extends RecyclerView.ViewHolder {

        public MsgListHolder(View itemView) {
            super(itemView);
        }
    }
}
