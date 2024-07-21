package com.dudu.wearlauncher.model;

import android.graphics.drawable.Icon;

import java.io.Serializable;

public class Notification implements Serializable {
    public Icon icon;
    public String appName;
    public String title;
    public String content;

    public Notification() {

    }

    public Notification(Icon icon, String appName, String title, String content) {

    }
}
