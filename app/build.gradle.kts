plugins {
    id("com.android.application")
    
}

android {
    namespace = "com.dudu.wearlauncher"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.dudu.wearlauncher"
        minSdk = 19
        targetSdk = 33
        versionCode = 1
        versionName = "Chip"
        multiDexEnabled = true
        vectorDrawables { 
            useSupportLibrary = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = false
        
    }
    
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //SwipeDrawer
    implementation("cn.Leaqi:SwipeDrawer:1.6")
    //glide
    implementation("com.github.bumptech.glide:glide:4.13.2")
    //Multidex
    implementation("androidx.multidex:multidex:2.0.1")
    //UtilCode
    implementation("com.blankj:utilcodex:1.31.1")
    //watchface-dev
    implementation(project(":watchface-dev-utils"))
    //bugly
    implementation("com.tencent.bugly:crashreport:4.1.9")
}
