plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.ceylonbotanica"
    compileSdk = 36   // ⬅️ updated

    defaultConfig {
        applicationId = "com.example.ceylonbotanica"
        minSdk = 24
        targetSdk = 36   // ⬅️ updated
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

    // Use Java 17 toolchain for AGP 8.x
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17   // ⬅️ updated
        targetCompatibility = JavaVersion.VERSION_17   // ⬅️ updated
    }
    kotlinOptions {
        jvmTarget = "17"   // ⬅️ updated
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.android.material:material:1.12.0")
}
