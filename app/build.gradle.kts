plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.quizzingapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quizzingapplication"
        minSdk = 24
        targetSdk = 34
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
    implementation(libs.espresso.contrib)

    testImplementation(libs.junit)
    testImplementation(libs.mockitoCore)
    androidTestImplementation(libs.truth)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espressoCore)
    androidTestImplementation(libs.mockitoAndroid)
    androidTestImplementation(libs.core)
    androidTestImplementation(libs.testRules)
}