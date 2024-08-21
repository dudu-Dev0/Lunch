package com.dudu.wearlauncher.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.ims.feature.MmTelFeature;
import com.dudu.wearlauncher.utils.ILog;
import com.dudu.wearlauncher.utils.MobileDataUtil;

public class MobileNetworkObserver {

    private Context mContext;
    private MNBroadcastReceiver receiver;
    private MNStateListener mMNStateListener;

    public MobileNetworkObserver(Context context) {
        mContext = context;
        receiver = new MNBroadcastReceiver();
    }

    public void register(MNStateListener listener) {
        mMNStateListener = listener;
        if (receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(receiver, filter);
            
        }
    }

    public void unregister() {
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }

    private class MNBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                    if(MobileDataUtil.isMobileDataEnabled(mContext)) {
                    	mMNStateListener.onStateEnabled();
                    }else{
                        mMNStateListener.onStateDisabled();
                    }
                }
            }
        }
    }

    public interface MNStateListener {

        void onStateDisabled();

        void onStateEnabled();
    }
}
