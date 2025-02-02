package com.dudu.wearlauncher.widget;
import android.util.Log;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


public class BubbleLayoutManager extends RecyclerView.LayoutManager {
    int ItemCounts;
    int TriangleD;
    int TriangleH;
    double Z;
    int centerX;
    int centerY;
    ComputeZ computeZ;
    boolean isScale;
    int itemX;
    int itemY;
    int pos;
    double r;
    float scale;
    BubbleHexagon bHexagon;
    int viewHeight;
    int viewWidth;
    int verticalScrollOffset = 0;
    int horizontalScrollOffset = 0;

    float maxX;
    float maxY;
    float minX;
    float minY;
    
    float hexHeight = maxY-minY;
    float hexWidth = maxX-minX;
    
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollVertically() {
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        detachAndScrapAttachedViews(recycler);
        checkItems(recycler);
        detachAndScrapAttachedViews(recycler);
        putItems(recycler);
    }/*
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 获取RecyclerView的当前高度和布局总高度
        int totalHeight = getHeight();
        int maxScroll = totalHeight-viewHeight/2; // 设定最大滑动范围

        // 如果dy导致滑动超过最大范围，则限制其值
        if (dy > 0) {
            dy = Math.min(dy, (int)maxScroll); // 向下滑动时的限制
        } else if (dy < 0) {
            dy = Math.max(dy, -(int)maxScroll); // 向上滑动时的限制
        }
        
        detachAndScrapAttachedViews(recycler);
        putItems(recycler);
        return super.scrollVerticallyBy(dy, recycler, state);
    }
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 获取RecyclerView的当前高度和布局总高度
        int totalWidth = getWidth();
        int maxScroll = totalWidth - viewWidth/2; 

        if (dx > 0) {
            dx = Math.min(dx, (int)maxScroll);
        } else if (dx < 0) {
            dx = Math.max(dx, -(int)maxScroll); 
        }
        
        detachAndScrapAttachedViews(recycler);
        putItems(recycler);
        return super.scrollHorizontallyBy(dx, recycler, state);
    }*/
    public int getPos() {
        return this.pos;
    }

    private void putItems(RecyclerView.Recycler recycler) {
        int i = 0;
        double d = 0.0d;
        int i2 = 0;
        while (i2 < getItemCount()) {
            View viewForPosition = recycler.getViewForPosition(i2);
            measureChildWithMargins(viewForPosition, i, i);
            addView(viewForPosition);
            float x = viewForPosition.getX() + (this.viewWidth / 2);
            float y = viewForPosition.getY() + (this.viewHeight / 2);
            
            this.computeZ = new ComputeZ(x, y, this.centerX, this.centerY, this.viewHeight);
            double d2 = d;
            this.Z = (this.r - Math.sqrt(Math.pow(x - this.centerX, 2.0d) + Math.pow(y - this.centerY, 2.0d))) / this.r;
            if (this.isScale) {
                float f = this.scale;
                if (f >= 0.0f) {
                    ViewCompat.animate(viewForPosition).setDuration(100L).scaleX(this.computeZ.Z*viewForPosition.getScaleX()).scaleY(this.computeZ.Z*viewForPosition.getScaleY()).alpha(this.computeZ.Z*1.25f).start();
                } else if (f < 0.0f) {
                    ViewCompat.animate(viewForPosition).setDuration(100L).scaleX(viewForPosition.getScaleX()).scaleY(viewForPosition.getScaleY()).alpha(1f).start();
                }
            } else {
                float f2 = this.scale;
                if (f2 >= 0.0f) {
                    viewForPosition.setScaleX(this.computeZ.Z*viewForPosition.getScaleX());
                    viewForPosition.setScaleY(this.computeZ.Z*viewForPosition.getScaleY());
                    viewForPosition.setAlpha(this.computeZ.Z*1.25f);
                } else if (f2 < 0.0f) {/*
                    viewForPosition.setScaleX(viewForPosition.get);
                    viewForPosition.setScaleY(0.7f);*/
                    viewForPosition.setAlpha(1f);
                }
            }
           // viewForPosition.setTranslationX(this.computeZ.dx);
            //viewForPosition.setTranslationY(this.computeZ.dy);
            double d3 = this.Z;
            if (d2 <= d3) {
                this.pos = i2;
                this.itemX = (int) x;
                this.itemY = (int) y;
                d = d3;
            } else {
                d = d2;
            }
            i2++;
            i = 0;
        }
    }

    private void checkItems(RecyclerView.Recycler recycler) {
        this.isScale = false;
        this.ItemCounts = getItemCount();
        this.centerX = getWidth() / 2;
        this.centerY = getHeight() / 2;
        View viewForPosition = recycler.getViewForPosition(0);
        measureChildWithMargins(viewForPosition, 10, 10);
        this.viewHeight = getDecoratedMeasuredHeight(viewForPosition);
        this.viewWidth = getDecoratedMeasuredWidth(viewForPosition);
        int i = this.viewHeight;
        this.TriangleD = i;
        this.TriangleH = (int) ((i * Math.sqrt(3.0d)) / 2.0d);
        this.r = Math.sqrt(Math.pow(this.centerX, 2.0d) + Math.pow(this.centerY, 2.0d));
        this.bHexagon = new BubbleHexagon(this.ItemCounts, this.centerX, this.centerY, this.TriangleD, this.TriangleH);
        for (int i2 = 0; i2 < this.ItemCounts; i2++) {
            View viewForPosition2 = recycler.getViewForPosition(i2);
            measureChildWithMargins(viewForPosition2, 10, 10);
            addView(viewForPosition2);
            layoutDecoratedWithMargins(viewForPosition2, this.bHexagon.x.get(i2).intValue() - (this.viewWidth / 2), this.bHexagon.y.get(i2).intValue() - (this.viewHeight / 2), this.bHexagon.x.get(i2).intValue() + (this.viewWidth / 2), this.bHexagon.y.get(i2).intValue() + (this.viewHeight / 2));
            ComputeZ computeZ = new ComputeZ(this.bHexagon.x.get(i2).intValue(), this.bHexagon.y.get(i2).intValue(), this.centerX, this.centerY, this.viewHeight);
            this.computeZ = computeZ;
            viewForPosition2.setScaleX(this.computeZ.Z*viewForPosition2.getScaleX());
            viewForPosition2.setScaleY(this.computeZ.Z*viewForPosition2.getScaleY());
            viewForPosition2.setAlpha(this.computeZ.Z*1.25f);
            //viewForPosition2.setTranslationX(this.computeZ.dx);
            //viewForPosition2.setTranslationY(this.computeZ.dy);
        }
        detachAndScrapAttachedViews(recycler);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(verticalScrollOffset+i>getHeight()/2+getPaddingBottom()||verticalScrollOffset+i< -getHeight()/2-getPaddingTop()) {
        	i = 0;
        }
        this.verticalScrollOffset += i;
        offsetChildrenVertical(-i);
        detachAndScrapAttachedViews(recycler);
        putItems(recycler);
        return i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if(horizontalScrollOffset+i>getHeight()/2+getPaddingStart()||horizontalScrollOffset+i< -getHeight()/2-getPaddingEnd()) {
        	i = 0;
        }
        this.horizontalScrollOffset += i;
        offsetChildrenHorizontal(-i);
        detachAndScrapAttachedViews(recycler);
        putItems(recycler);
        return i;
        
    }
    public class ComputeZ {
        float Z;
        double d;
        int dx;
        int dy;
        double r;
        double r1;

        public ComputeZ(float f, float f2, int i, int i2, int i3) {
            double sqrt = Math.sqrt(Math.pow(i, 2.0d) + Math.pow(i2, 2.0d));
            this.r = sqrt;
            this.r1 = sqrt * 0.25d;
            float f3 = f - i;
            float f4 = f2 - i2;
            double sqrt2 = Math.sqrt(Math.pow(f3, 2.0d) + Math.pow(f4, 2.0d));
            this.d = sqrt2;
            if (sqrt2 >= this.r1) {
                this.Z = (float) (1.0d - (sqrt2 / this.r));
            } else {
                this.Z = 0.75f;
            }
            float f5 = i3 * (0f);
            double d = this.r;
            this.dx = (int) ((f3 * f5) / d);
            this.dy = (int) ((f5 * f4) / d);
        }
    }
    
    public class BubbleHexagon {
        int floor;
        int floors;
        int index;
        int items;
        int side;
        int triangleD;
        int triangleH;
        List<Integer> x = new ArrayList();
        List<Integer> y = new ArrayList();

        public BubbleHexagon(int i, int i2, int i3, int i4, int i5) {
            this.triangleD = i4;
            this.triangleH = i5;
            this.items = i;
            this.floors = 0;
            while (true) {
                int i6 = this.floors;
                if ((i6 * 3 * i6) + (i6 * 3) + 1 >= this.items) {
                    break;
                }
                this.floors = i6 + 1;
            }
            this.x.add(0, Integer.valueOf(i2));
            this.y.add(0, Integer.valueOf(i3));
            this.floor = 0;
            int i7 = 1;
            while (this.floor <= this.floors) {
                while (this.side <= 5) {
                    while (this.index <= this.floor) {
                        Log.i("TEXT", "index=" + this.index + "side=" + this.side + "floor=" + this.floor + "i=" + i7);
                        int i8 = this.side;
                        if (i8 == 0) {
                            this.x.add(i7, Integer.valueOf((this.index * i5) + i2));
                            this.y.add(i7, Integer.valueOf((i3 - ((this.floor + 1) * i4)) + ((this.index * i4) / 2)));
                        } else if (i8 == 1) {
                            this.x.add(i7, Integer.valueOf(((this.floor + 1) * i5) + i2));
                            this.y.add(i7, Integer.valueOf((i3 - (((this.floor + 1) * i4) / 2)) + (this.index * i4)));
                        } else if (i8 == 2) {
                            this.x.add(i7, Integer.valueOf((((this.floor + 1) * i5) + i2) - (this.index * i5)));
                            this.y.add(i7, Integer.valueOf((((this.floor + 1) * i4) / 2) + i3 + ((this.index * i4) / 2)));
                        } else if (i8 == 3) {
                            this.x.add(i7, Integer.valueOf(i2 - (this.index * i5)));
                            this.y.add(i7, Integer.valueOf((((this.floor + 1) * i4) + i3) - ((this.index * i4) / 2)));
                        } else if (i8 == 4) {
                            this.x.add(i7, Integer.valueOf(i2 - ((this.floor + 1) * i5)));
                            this.y.add(i7, Integer.valueOf(((((this.floor + 1) * i4) / 2) + i3) - (this.index * i4)));
                        } else if (i8 == 5) {
                            this.x.add(i7, Integer.valueOf((i2 - ((this.floor + 1) * i5)) + (this.index * i5)));
                            this.y.add(i7, Integer.valueOf((i3 - (((this.floor + 1) * i4) / 2)) - ((this.index * i4) / 2)));
                        }
                        this.index++;
                        i7++;
                        if (i7 == this.items) {
                            break;
                        }
                    }
                    this.index = 0;
                    this.side++;
                    if (i7 == this.items) {
                        break;
                    }
                }
                this.side = 0;
                this.floor++;
                if (i7 == this.items) {
                    return;
                }
            }
        }
    }
}