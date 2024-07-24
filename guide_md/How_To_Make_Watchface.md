# 表盘编写教程

## 准备工作
首先，表盘本身其实就是一个zip文件
其中的结构如下
> [watchface-name].zip
>> preview.png\
>> manifest.json\
>> [watchface-name].wf\
Launcher在导入表盘时会将表盘解压至`/sdacrd/Android/data/com.dudu.wearlauncher/files/watchface`目录下
### 搭建一个Android开发环境
请自行配置`Android Studio、AndroidIDE、AIDE`之类的IDE
### 新建一个Android项目
各项配置随你喜好
注意要使用Kotlin DSL(.kts) Gradle(便于下一步添加构建脚本)
然后关闭混淆（开启混淆会影响启动器寻找表盘）
### 配置构建脚本
在项目根目录下新建一个module
注意这里module的名字将会作为表盘的唯一标识符，请注意不要和已有表盘一样，否则会产生冲突
在module根目录下有一个build.gradle.kts文件
打开后在`android`闭包下有个`defaultConfig`闭包里面有`applicationId`，这里的值是表盘的包名，将会被写入`manifest.json`中
我们在文件尾部插入如下代码
```kotlin
//表盘在桌面内显示的名称
val watchfaceName = "数码太空人"
//表盘作者
val author = "dudu"

tasks.register<Zip>("packageDebugWatchface") {
    group = "build"
    description = "构建debug表盘文件"
    dependsOn("assembleDebug","copyPreviewImageDebug")
    doFirst{
        val apkDir = file("${buildDir}/outputs/apk/debug")
        val original = apkDir.walkTopDown().filter { it.extension == "apk" }.toList().get(0);
        val wf = file("${buildDir}/outputs/apk/debug/${project.name}.wf")
        file("${buildDir}/outputs/apk/debug/output-metadata.json").delete()
        println("Creating WF...")
        original.renameTo(wf)
        val manifestContent = """
            {
                "name": "${project.name}",
                "packageName": "${android.defaultConfig.applicationId}",
                "displayName": "${watchfaceName}",
                "versionCode": ${android.defaultConfig.versionCode},
                "author": "${author}"
            }
        """.trimIndent()
        val manifestFile = file("${buildDir}/outputs/apk/debug/manifest.json")
        manifestFile.writeText(manifestContent)
        println("Write manifest file SUCCESSFUL")
    }
    from("${buildDir}/outputs/apk/debug")
    archiveFileName.set("${project.name}.zip")
    destinationDirectory.set(file("${buildDir}/outputs/watchface/debug"))
    doLast{
        println("Watchface created successful at ${buildDir}/outputs/watchface/debug/${project.name}.zip !")
    }
}

tasks.register<Zip>("packageReleaseWatchface") {
    group = "build"
    description = "构建release表盘文件"
    dependsOn("assembleRelease","copyPreviewImageRelease")
    doFirst{
        val apkDir = file("${buildDir}/outputs/apk/release")
        val original = apkDir.walkTopDown().filter { it.extension == "apk" }.toList().get(0);
        val wf = file("${buildDir}/outputs/apk/release/${project.name}.wf")
        file("${buildDir}/outputs/apk/release/output-metadata.json").delete()
        println("Creating WF...")
        original.renameTo(wf)
        val manifestContent = """
            {
                "name": "${project.name}",
                "packageName": "${android.defaultConfig.applicationId}",
                "displayName": "${watchfaceName}",
                "versionCode": ${android.defaultConfig.versionCode},
                "author": "${author}"
            }
        """.trimIndent()
        val manifestFile = file("${buildDir}/outputs/apk/release/manifest.json")
        manifestFile.writeText(manifestContent)
        println("Write manifest file SUCCESSFUL")
    }
    from("${buildDir}/outputs/apk/release")
    archiveFileName.set("${project.name}.zip")
    destinationDirectory.set(file("${buildDir}/outputs/watchface/release"))
    doLast{
        println("Watchface created successful at ${buildDir}/outputs/watchface/release/${project.name}.zip !")
    }
}
tasks.register<Copy>("copyPreviewImageDebug") {
    mustRunAfter("createDebugApkListingFileRedirect")
    from("src/main/assets")
    into("${buildDir}/outputs/apk/debug")
    include("preview.png")
}

tasks.register<Copy>("copyPreviewImageRelease") {
    mustRunAfter("createReleaseApkListingFileRedirect")
    from("src/main/assets")
    into("${buildDir}/outputs/apk/release")
    include("preview.png")
}

```
注意修改`watchfaceName`和`author`这将会在Launcher中显示
将`watchface-dev-utils.aar`放在module的libs文件夹下
并在build.gradle.kts中的`dependencies`闭包下加入
```kotlin
implementation(files("./libs/watchface-dev-utils.aar"))
```

最后在项目的settings.gradle.kts导入module
```kotlin
include(":<module-name>")
```
> 以上工作还可以通过直接clone watchface-temple来完成😉

## 开发表盘
在`./<your-module-name>/src/main/java/<your-package-name>/`下新建java文件,
名叫`WatchFaceImpl`继承自`com.dudu.wearlauncher.model.WatchFace`并重写父类的构造方法
```java
    public WatchFaceImpl(Context context, AttributeSet attributeSet, int i,String path) {
        super(context, attributeSet, i,path);
    }


    public WatchFaceImpl(Context context,String path) {
        this(context, null,path);
    }

    public WatchFaceImpl(Context context, AttributeSet attributeSet,String path) {
        this(context, attributeSet,0,path);
    }

```
添加方法`getWatchFace()`
>Launcher将会通过这个函数获取表盘，其中context是启动器的context，str是表盘`.wf`文件的绝对路径

```java
    public static FrameLayout getWatchFace(Context context, String str) {
        return new WatchFaceImpl(context,str);
    }
```
最后重写`initView()`方法，这个函数将会自动被调用，所以请在其中写表盘主要逻辑
### 加载布局
请使用`LayoutInflator`加载布局
例：
```java
LayoutInflater.from(getHostContext()).inflate(getResources().getLayout(R.layout.layout_main), this);
```
> 需要注意的是获取所有资源文件请使用getResource()对应方法
> 例如drawable资源：getResources().getDrawable(R.drawable.your_drawable)
> 千万不要直接使用id值获取资源（findView除外）
### 获取View
布局中的view可以直接使用`findViewById(R.id.your_view)`获取

### 关于WatchFace中的一些方法
1. `updateBattery(int i)`无需自行调用，该方法会在电池更新时被启动器调用，`i`为电池电量，可以重写该方法以实现电量更新
2. `updateStep(int i)`无需自行调用，和`updateBattery`一样，可以实现步数更新
3. `updateTime()`同上，可实现时间更新，有这些方法，表盘不需要自行设置监听器
4. `getResource()`返回一个Resource对象，用于获取表盘自带资源文件
5. `getHostContext()`返回一个Context对象，它是启动器的Context，可以用于一些需要context的操作

## 总结
以上就是表盘开发的一些注意事项，如果您有Android开发经验，表盘开发一定会更加容易。你也可以参考启动器的[表盘示例](https://github.com/dudu-Dev0/WearLauncher/tree/main/watchface-example/src/main/java/com/dudu/watchface/example)
