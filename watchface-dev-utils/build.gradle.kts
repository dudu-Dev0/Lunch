plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.dudu.wearlauncher"
    compileSdk = 33
    defaultConfig {
        minSdk = 19
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
    


dependencies {
    //UtilCode
    implementation("com.blankj:utilcodex:1.31.1")
    implementation("androidx.core:core-ktx:1.10.1")
}
