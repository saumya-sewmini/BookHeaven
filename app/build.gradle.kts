plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.bookheaven"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bookheaven"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("io.coil-kt:coil-gif:2.6.0")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.29")
    implementation ("com.google.android.material:material:1.10.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.github.akshaaatt:Sliding-Carousel:1.0.4")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.google.code.gson:gson:2.8.9")

}