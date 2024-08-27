
plugins {
    id("com.android.application")
}

//表盘在桌面内显示的名称
val watchfaceName = "数码太空人"
//表盘作者
val author = "dudu"

android {
    namespace = "com.dudu.watchface.example"
    compileSdk = 33 
    defaultConfig {
        applicationId = "com.dudu.watchface.example"
        minSdk = 19
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = false
        buildConfigField("String","DISPLAY_NAME","\"$watchfaceName\"")
        buildConfigField("String","AUTHOR","\"$author\"")
        buildConfigField("String","WATCHFACE_NAME","\"${project.name}\"")
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures.buildConfig=true
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    
}

dependencies {
    //watchface-dev
    compileOnly(project(":watchface-dev-utils"))
}


    
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
tasks.whenTaskAdded {
    if (name == "createDebugApkListingFileRedirect" || name == "createReleaseApkListingFileRedirect") enabled = false
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

