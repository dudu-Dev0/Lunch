# 使用View构建您的表盘
上一节中我们已经创建了表盘入口类WatchFaceImpl\
这一节我们将尝试使用加载布局的方法创建表盘

## 实现表盘
我们在app/src/main/res/layout下新建文件并写入以下代码\
`layout_watchface.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/black"
    android:orientation="vertical">
    <TextClock
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:textColor="@android:color/white"/>
    <TextView
        android:id="@+id/battery_tv"
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        android:textColor="@android:color/white"/>
</LinearLayout>
```
这里我们放了一个TextClock用于显示时间\
还有一个TextView用于显示电量\
接着我们在WatchFaceImpl中重写initView()方法\
并使用LayoutInflator填充布局\
`WatchFaceImpl.java`
```java
public class WatchFaceImpl extends WatchFace {
    TextView batteryTextView;
    public WatchFaceImpl(Context context){  
        super(context);
    }
    
    @Override
    public void initView() {
        super.initView();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_layout_watchface, this);
        batteryTextView = findViewById(R.id.battery_tv);
    }
}
```
最后我们重写updateBattery(int i,int status)方法以实现电量的更新
```java
public class WatchFaceImpl extends WatchFace {
    ...
    @Override
    public void updateBattery(int i,int status){
        batteryTextView.setText("电量"+i+"%");
    }
    ...
}
```
这样，我们就实现好了一个最简单的表盘\
可以发现，Lunch表盘开发其实和平常Android开发并没有什么不同的地方
## WatchFace中的一些方法

WatchFace类中提供了一些方便开发的方法，现在让我们研究一下
| 方法 | 用途 | 用法 |
| :-- | :-- | :--: |
| `getContext()` | 返回表盘的Context对象，可用于打开Activity等操作 | 直接调用 |
| `initView()` | 表盘加载时调用，可在其中编写表盘主要逻辑 | 重写 |
| `updateTime()` | 每一分钟调用一次，可用于更新时间 | 重写 |
| `updateStep(int step)` | (未在启动器中实现)step为当前步数，用于步数更新 | 重写 |
| `updateBattery(int i,int status)` | i为电量百分比，status为电池状态，详见[BatteryManager](https://developer.android.com/reference/android/os/BatteryManager),可用于步数更新 | 重写 |
| `onWatchfaceVisibilityChanged(boolean isVisible)` | 在表盘可见性发生变化时调用，isVisible为表盘是否可见，一般用于表盘省电 | 重写 |
| `onScreenStateChanged(boolean isScreenOn)` | 在屏幕状态变化时被调用，isScreenOn为屏幕的亮起状态 | 重写 |

## 总结
现在你已经学会了简单表盘的开发\
发挥你的想象力去创造你想要的的表盘吧


<br />
<p
    align="right">下一步：
</p>
<div align="right" >
<a href="">使用Canvas构建您的表盘</a>
</div>