plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.recipe"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.recipe"
        minSdk = 24
        targetSdk = 36
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


}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    val room_version = "2.8.3"

    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-rxjava3:$room_version")
    annotationProcessor("androidx.room:room-compiler:2.8.3")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:13.0.0")

    implementation("com.google.android.material:material:1.10.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}