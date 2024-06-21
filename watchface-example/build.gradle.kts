
plugins {
    id("com.android.application")
    
}

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
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    
}

dependencies {
    //watchface-dev
    implementation(project(":watchface-dev-utils"))
}

tasks.register<Zip>("packageDebugWatchface") {
    dependsOn("assembleDebug")
    doFirst{
        val original = file("${buildDir}/outputs/apk/debug/${project.name}-debug.apk")
        val wf = file("${buildDir}/outputs/apk/debug/${project.name}.wf")
        file("${buildDir}/outputs/apk/debug/output-metadata.json").delete()
        original.renameTo(wf)
    }
    from("${buildDir}/outputs/apk/debug")
    archiveFileName.set("${project.name}.zip")
    destinationDirectory.set(file("${buildDir}/outputs/watchface/debug"))
    
}

tasks.register<Zip>("packageReleaseWatchface") {
    dependsOn("assembleRelease")
    doFirst{
        val original = file("${buildDir}/outputs/apk/release/${project.name}-release.apk")
        val wf = file("${buildDir}/outputs/apk/release/${project.name}.wf")
        file("${buildDir}/outputs/apk/release/output-metadata.json").delete()
        original.renameTo(wf)
    }
    from("${buildDir}/outputs/apk/release")
    archiveFileName.set("${project.name}.zip")
    destinationDirectory.set(file("${buildDir}/outputs/watchface/release"))
}
