# 表盘开发指南

Lunch具有高度自定义的特性\
现在让我们来学习表盘的开发

## 要求
- 一台能够流畅运行[Android Studio](https://developer.android.google.cn/studio?hl=zh-cn)的电脑
- 或者一台能够运行[AndroidIDE](https://m.androidide.com/)的手机

## 开始之前
Lunch的表盘系统基于Android View实现\
这需要您拥有Android开发基础\
若您还是一个Android开发小白 浏览本篇文章时可能会比较吃力

## 创建项目
打开您的IDE，创建一个Android应用项目\
您可以自由配置您的应用包名等信息\
静待Gradle Sync完成...\
恭喜🎉您已经成功创建了您的第一个表盘项目

## 配置表盘必要信息
在Gradle中引入表盘库,记得引入完Sync一下\
`build.gradle.kts`
```kotlin
...
dependencies {
    ...
    implementation(files("./libs/watchface-dev-utils.aar"))
    implementation("com.blankj:utilcodex:1.31.1")
}
...
```
接下来在app/src/main/java/<packageName>下创建一个新的类，名字随意，这里使用`WatchFaceImpl`\
`WatchFaceImpl.java`
```java
public class WatchFaceImpl extends WatchFace {
    /**
    * 注意一定要添加这个单参数的构造方法
    * 这个构造方法用于启动器创建表盘
    */
    public WatchFaceImpl(Context context){  
        super(context);
    }
}
```
这里我们让WatchFaceImpl继承自WatchFace这个类，这个类将会在后文进行解释，这里不过多赘述\
接下来在AndroidManifest.xml中添加如下代码\
`AndroidManifest.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest
    ...>

    <application
        ...>
        <activity
            android:name=".StubActivity"
            android:exported="true">
            <!--这个Activity声明仅作占位用，可不创建，仅用于启动器寻找表盘-->
            <intent-filter>
                <action
                    android:name="com.dudu.wearlauncher.WATCHFACE" />
                <category
                    android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>
    </application>
    ...
</manifest>
```
最后创建app/src/main/res/xml/watchface_config.xml添加如下代码\
`watchface_config.xml`
```xml
<config
    <!--表盘的预览图片-->
    preview="@drawable/preview"
    <!--表盘的名称-->
    name="<watchFaceName>"
    <!--表盘入口类-->
    watchface="<包名>.WatchFaceImpl"
    <!--表盘设置Activity(这里先不设置)-->
    settings_activity=""
/>
```
## 小结
到这里你已经搭建好了表盘项目的基本框架

<br />
<p
    align="right">下一步：
</p>
<div align="right" >
<a href="https://github.com/dudu-Dev0/Lunch/blob/main/How-To-Make-WatchFace/Make-By-View.MD">使用View构建您的表盘</a>
<br />
<a href="">使用Canvas构建您的表盘</a>
</div>