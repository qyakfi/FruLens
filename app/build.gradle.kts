plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.deboo.frulens"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.deboo.frulens"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        mlModelBinding = true
    }

    packaging {
        jniLibs.keepDebugSymbols.add("lib/arm64-v8a/libtask_vision_jni.so")
        jniLibs.keepDebugSymbols.add("lib/armeabi-v7a/libtask_vision_jni.so")
        jniLibs.keepDebugSymbols.add("lib/x86/libtask_vision_jni.so")
        jniLibs.keepDebugSymbols.add("lib/x86_64/libtask_vision_jni.so")
        jniLibs.keepDebugSymbols.add("lib/arm64-v8a/libtensorflowlite_jni.so")
        jniLibs.keepDebugSymbols.add("lib/armeabi-v7a/libtensorflowlite_jni.so")
        jniLibs.keepDebugSymbols.add("lib/x86/libtensorflowlite_jni.so")
        jniLibs.keepDebugSymbols.add("lib/x86_64/libtensorflowlite_jni.so")
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //implementation(libs.androidx.core.splashscreen)

    // TensorFlow Lite dependencies
    implementation(libs.tensorflow.lite.task.vision)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.tensorflow.lite)
}
