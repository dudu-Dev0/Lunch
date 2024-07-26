package com.dudu.wearlauncher.model;

import android.graphics.drawable.Icon;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class Notification implements Parcelable {
    public Icon icon;
    public String appName;
    public String title;
    public String content;
    public String key;
    public static final Creator<Notification> CREATOR = new Creator<>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
    public Notification() {

    }
    public long time;

    public Notification(Icon icon, String appName, String title, String content, long time, String key) {
        this.icon = icon;
        this.appName = appName;
        this.title = title;
        this.content = content;
        this.time = time;
        this.key = key;
    }

    protected Notification(Parcel in) {
        icon = in.readParcelable(Icon.class.getClassLoader());
        content = in.readString();
        appName = in.readString();
        title = in.readString();
        time = in.readLong();
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(icon, i);
        parcel.writeString(content);
        parcel.writeString(appName);
        parcel.writeString(title);
        parcel.writeLong(time);
        parcel.writeString(key);
    }
}
