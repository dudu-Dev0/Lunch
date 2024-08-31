package com.dudu.wearlauncher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;

// 3??? Factory ? Factory2 ??? Factory
public class PluginInflaterFactory implements LayoutInflater.Factory, LayoutInflater.Factory2 {
    private static final String TAG = "PluginInflaterFactory";
    private LayoutInflater.Factory mBaseFactory;
    private LayoutInflater.Factory2 mBaseFactory2;
    private ClassLoader mClassLoader;

    public PluginInflaterFactory(LayoutInflater.Factory base, ClassLoader classLoader) {
        if (null == classLoader) {
            throw new IllegalArgumentException("classLoader is null");
        }
        mBaseFactory = base;
        mClassLoader = classLoader;
    }

    public PluginInflaterFactory(LayoutInflater.Factory2 base2, ClassLoader classLoader) {
        if (null == classLoader) {
            throw new IllegalArgumentException("classLoader is null");
        }

        mBaseFactory2 = base2;
        mClassLoader = classLoader;
    }

    // 4??? onCreateView() ??
    @Override
    public View onCreateView(String s, Context context, AttributeSet attributeSet) {
        if (!s.contains(".")) {
            return null;
        }

        View v = getView(s, context, attributeSet);
        if (v != null) {
            return v;
        }

        if (mBaseFactory != null && !(mBaseFactory instanceof PluginInflaterFactory)) {
            v = mBaseFactory.onCreateView(s, context, attributeSet);
        }

        return v;
    }

    // 4??? onCreateView() ??
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (!name.contains(".")) {
            return null;
        }

        View v = getView(name, context, attrs);
        if (v != null) {
            return v;
        }

        if (mBaseFactory2 != null && !(mBaseFactory2 instanceof PluginInflaterFactory)) {
            v = mBaseFactory2.onCreateView(parent, name, context, attrs);
        }

        return v;
    }

    // 5????? ClassLoader ?? View ???
    private View getView(String name, Context context, AttributeSet attrs) {
        View v = null;
        try {
            Class<?> clazz = mClassLoader.loadClass(name);
            Constructor<?> c = clazz.getConstructor(Context.class, AttributeSet.class);
            v = (View) c.newInstance(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }
}
