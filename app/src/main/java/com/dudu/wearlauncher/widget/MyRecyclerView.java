package com.dudu.wearlauncher.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.xtc.widget.scalablecontainer.AppRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerView extends AppRecyclerView {
    private View mEmptyView;
    public static final int SCALE_TYPE_TOP = 1 << 0;
    public static final int SCALE_TYPE_BOTTOM = 1 << 1;
    public static final int SCALE_TYPE_LEFT = 1 << 2;
    public static final int SCALE_TYPE_RIGHT = 1 << 3;
    public static final int SCALE_TYPE_BOTTOM_FOR_LINEAR = 1 << 4;
    private int scaleType = 0;

    public void setScaleType(int type) {
    	scaleType = type;
    }
    
    private boolean hasType(int type) {
    	return (scaleType & type)!=0;
    }
    
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
        scaleCenter();
    }

    private void scaleCenter(){
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        int contentHeight = getHeight();
        int contentWidth = getWidth();
        for (View view : listChild()) {
            if (view.getVisibility() == View.VISIBLE) {
                int top = view.getTop();
                int bottom = view.getBottom();
                int left = view.getLeft();
                int right = view.getRight();
                int height = view.getHeight();
                int width = view.getWidth();
                if (bottom >= scrollY+contentHeight) {
                    if (top <= scrollY+contentHeight&&hasType(SCALE_TYPE_BOTTOM)) {
                        float f = (scrollY + contentHeight - top)/(float)height;
                        view.setPivotX(((float) width) / 2.0f);
                        view.setPivotY(0f);
                        view.setScaleX(f);
                        view.setScaleY(f);
                    }
                }
                if (bottom >= scrollY) {
                    if (top <= scrollY + measuredHeight&&hasType(SCALE_TYPE_BOTTOM_FOR_LINEAR)) {
                        top = (bottom <= scrollY + measuredHeight || top >= scrollY + measuredHeight) ? height : scrollY + measuredHeight - top;
                        float f = ((((float) top) * 0.2f) / ((float) height)) + 0.8f;
                        view.setPivotX(((float) width) / 2.0f);
                        view.setPivotY(0f);
                        view.setScaleX(f);
                        view.setScaleY(f);
                    }
                }
                if (bottom >= scrollY) {
                    if (top <= scrollY&&hasType(SCALE_TYPE_TOP)) {
                        float f = (bottom - scrollY)/(float)height;
                        view.setPivotX(((float) width) / 2.0f);
                        view.setPivotY((float)height);
                        view.setScaleX(f);
                        view.setScaleY(f);
                    }
                }
                if (right >= scrollX) {
                    if (left <= scrollX&&hasType(SCALE_TYPE_LEFT)) {
                        float f = (right - scrollX)/(float)width;
                        view.setPivotX((float)width);
                        view.setPivotY((float)height/2);
                        view.setScaleX(f);
                        view.setScaleY(f);
                    }
                }
                if (right >= scrollX+measuredWidth) {
                    if (left <= scrollX+measuredWidth&&hasType(SCALE_TYPE_RIGHT)) {
                        float f = (scrollX+measuredWidth - left)/(float)width;
                        view.setPivotX(0f);
                        view.setPivotY((float)height/2);
                        view.setScaleX(f);
                        view.setScaleY(f);
                    }
                }
                if(left > scrollX && right < scrollX + measuredWidth && top > scrollY && bottom < scrollY + measuredHeight) {
                    view.setScaleX(1f);
                    view.setScaleY(1f);
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
