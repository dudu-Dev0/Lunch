package com.dudu.wearlauncher.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.dudu.wearlauncher.utils.OverScrollDelegate;
import com.xtc.widget.scalablecontainer.AppRecyclerView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MyRecyclerView extends AppRecyclerView {
    private View mEmptyView;

    private AdapterDataObserver emptyObserver =
            new AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    check();
                }
                
                @Override
                public void onItemRangeRemoved(int arg0, int arg1) {
                    super.onItemRangeRemoved(arg0, arg1);
                    check();
                }
                
                @Override
                public void onItemRangeInserted(int arg0, int arg1) {
                    super.onItemRangeInserted(arg0, arg1);
                    check();
                }
                
                @Override
                public void onItemRangeMoved(int arg0, int arg1, int arg2) {
                    super.onItemRangeMoved(arg0, arg1, arg2);
                    check();
                }
                
                public void check() {
                    Adapter<?> adapter =
                            getAdapter(); 
                    if (adapter != null && mEmptyView != null) {
                        if (adapter.getItemCount() == 0) {
                            mEmptyView.setVisibility(View.VISIBLE);
                            MyRecyclerView.this.setVisibility(View.GONE);
                        } else {
                            mEmptyView.setVisibility(View.GONE);
                            MyRecyclerView.this.setVisibility(View.VISIBLE);
                        }
                    }
                }
            };

    public MyRecyclerView(Context context) {
        this(context,null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }
    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
    }
    

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        scaleBottom();
        scaleTop();
        scaleLeft();
        scaleRight();
    }


    
    private void scaleBottom() {
        int scrollY = getScrollY();
        int measuredHeight = getMeasuredHeight();
        int contentHeight = getHeight();
        for (View view : listChild()) {
            if (view.getVisibility() == 0) {
                int top = view.getTop();
                int bottom = view.getBottom();
                int height = view.getHeight();
                int width = view.getWidth();
                int i = scrollY + measuredHeight;
                if (bottom >= scrollY+contentHeight) {
                    if (top <= scrollY+contentHeight) {
                        float f = (scrollY + contentHeight - top)/(float)height;
                        view.setPivotX(((float) width) / 2.0f);
                        view.setPivotY(0f);
                        view.setScaleX(f);
                        view.setScaleY(f);
                    }
                }else{
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                }
            }
        }
    }
    
    private void scaleBottomFprLinear() {
        int scrollY = getScrollY();
        int measuredHeight = getMeasuredHeight();
        for (View view : listChild()) {
            if (view.getVisibility() == 0) {
                int top = view.getTop();
                int bottom = view.getBottom();
                int height = view.getHeight();
                int width = view.getWidth();
                int i = scrollY + measuredHeight;
                if (bottom >= scrollY) {
                    if (top <= i) {
                        top = (bottom <= i || top >= i) ? height : i - top;
                        float f = ((((float) top) * 0.2f) / ((float) height)) + 0.8f;
                        view.setPivotX(((float) width) / 2.0f);
                        view.setPivotY(0f);
                        view.setScaleX(f);
                        view.setScaleY(f);
                    }
                }
            }
        }
    }
    private void scaleTop() {
        int scrollY = getScrollY();
        int measuredHeight = getMeasuredHeight();
        for (View view : listChild()) {
            if (view.getVisibility() == 0) {
                int top = view.getTop();
                int bottom = view.getBottom();
                int height = view.getHeight();
                int width = view.getWidth();
                int i = scrollY;
                if (bottom >= scrollY) {
                    if (top <= scrollY) {
                        float f = (bottom - scrollY)/(float)height;
                        view.setPivotX(((float) width) / 2.0f);
                        view.setPivotY((float)height);
                        view.setScaleX(f);
                        view.setScaleY(f);
                    }
                }else{
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                }
            }
        }
    }
    private void scaleLeft() {
        int scrollX = getScrollX();
        int measuredWidth = getMeasuredWidth();
        for (View view : listChild()) {
            if (view.getVisibility() == 0) {
                int left = view.getLeft();
                int right = view.getRight();
                int height = view.getHeight();
                int width = view.getWidth();
                int i = scrollX;
                if (right >= scrollX) {
                    if (left <= scrollX) {
                        float f = (right - scrollX)/(float)width;
                        view.setPivotX((float)width);
                        view.setPivotY((float)height/2);
                        view.setScaleX(f);
                        view.setScaleY(f);
                    }
                }else{
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                }
            }
        }
    }
    
    private void scaleRight() {
        int scrollX = getScrollX();
        int measuredWidth = getMeasuredWidth();
        for (View view : listChild()) {
            if (view.getVisibility() == 0) {
                int left = view.getLeft();
                int right = view.getRight();
                int height = view.getHeight();
                int width = view.getWidth();
                int i = scrollX+measuredWidth;
                if (right >= i) {
                    if (left <= i) {
                        float f = (i - left)/(float)width;
                        view.setPivotX(0f);
                        view.setPivotY((float)height/2);
                        view.setScaleX(f);
                        view.setScaleY(f);
                    }
                }
            }
        }
    }
    
    
    private List<View> listChild() {
        int childCount = getChildCount();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < childCount; i++) {
            arrayList.add(getChildAt(i));
        }
        return arrayList;
    }
}
