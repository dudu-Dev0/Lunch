package com.dudu.wearlauncher.utils;

import android.content.Context;
import android.content.pm.ResolveInfo;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.WearLauncherApp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PackageManagerEx {
    public static List<ResolveInfo> getAppList(Context context) {

        List<ResolveInfo> appList = PmUtils.getAllApps();
        Iterator<ResolveInfo> iterator = appList.iterator();
        List<String> hiddenList = new ArrayList();
        hiddenList.addAll(
                Arrays.asList(
                        context.getResources().getStringArray(R.array.hidden_activities)));
        ILog.w(
                "hiddenActivities:"
                        + SharedPreferencesUtil.getData(
                                SharedPreferencesUtil.HIDDEN_ACTIVITIES, ""));
        hiddenList.addAll(
                Arrays.asList(
                        ((String)
                                        SharedPreferencesUtil.getData(
                                                SharedPreferencesUtil.HIDDEN_ACTIVITIES, ""))
                                .split(":")));
        while (iterator.hasNext()) {
            ResolveInfo info = iterator.next();
            if (hiddenList.contains(info.activityInfo.packageName + "/" + info.activityInfo.name)) {
                iterator.remove();
            }
        }
        return appList;
    }
    public static List<String> getAppComponentList(Context context) {
        return getAppList(context).stream().map(app->app.activityInfo.packageName+"/"+app.activityInfo.name).collect(Collectors.toList());
    }
}
