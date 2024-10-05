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

public class MyRecyclerView extends RecyclerView implements OverScrollDelegate.OverScrollable {
    private View mEmptyView;
    private OverScrollDelegate mOverScrollDelegate;

    private AdapterDataObserver emptyObserver =
            new AdapterDataObserver() {
                @Override
                public void onChanged() {
                    Adapter<?> adapter =
                            getAdapter(); // ??????????????ListView???????????????????????
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
        super(context);
        createOverScrollDelegate();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createOverScrollDelegate();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        createOverScrollDelegate();
    }

    /** * @param emptyView ????view */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
    }

    // ===========================================================
    // OverScrollable, aim to call view internal methods
    // ===========================================================

    @Override
    public int superComputeVerticalScrollExtent() {
        return super.computeVerticalScrollExtent();
    }

    @Override
    public int superComputeVerticalScrollOffset() {
        return super.computeVerticalScrollOffset();
    }

    @Override
    public int superComputeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override
    public void superOnTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
    }

    @Override
    public void superDraw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public boolean superAwakenScrollBars() {
        return super.awakenScrollBars();
    }

    @Override
    public boolean superOverScrollBy(
            int deltaX,
            int deltaY,
            int scrollX,
            int scrollY,
            int scrollRangeX,
            int scrollRangeY,
            int maxOverScrollX,
            int maxOverScrollY,
            boolean isTouchEvent) {
        return super.overScrollBy(
                deltaX,
                deltaY,
                scrollX,
                scrollY,
                scrollRangeX,
                scrollRangeY,
                maxOverScrollX,
                maxOverScrollY,
                isTouchEvent);
    }

    // ===========================================================
    // createOverScrollDelegate
    // ===========================================================
    private void createOverScrollDelegate() {
        mOverScrollDelegate = new OverScrollDelegate(this);
    }

    // ===========================================================
    // Delegate
    // ===========================================================
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mOverScrollDelegate.onInterceptTouchEvent(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOverScrollDelegate.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        mOverScrollDelegate.draw(canvas);
    }

    @Override
    protected boolean overScrollBy(
            int deltaX,
            int deltaY,
            int scrollX,
            int scrollY,
            int scrollRangeX,
            int scrollRangeY,
            int maxOverScrollX,
            int maxOverScrollY,
            boolean isTouchEvent) {
        return mOverScrollDelegate.overScrollBy(
                deltaX,
                deltaY,
                scrollX,
                scrollY,
                scrollRangeX,
                scrollRangeY,
                maxOverScrollX,
                maxOverScrollY,
                isTouchEvent);
    }

    @Override
    public OverScrollDelegate getOverScrollDelegate() {
        return mOverScrollDelegate;
    }

    @Override
    public View getOverScrollableView() {
        return this;
    }

    @Override
    public boolean fling(int arg0, int arg1) {
        if(super.fling(arg0,arg1))mOverScrollDelegate.recyclerViewAbsorbGlows(arg0,arg1);
        return super.fling(arg0,arg1);
    }
}
