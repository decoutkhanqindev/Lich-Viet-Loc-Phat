import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.decoutkhanqindev.lich_viet_loc_phat"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.decoutkhanqindev.lich_viet_loc_phat"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "v1.0.1"
    }

    val localProperties = Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) file.inputStream().use { load(it) }
    }

    signingConfigs {
        create("release") {
            localProperties.getProperty("signing.store.file")?.let { storeFile = file(it) }
            storePassword = localProperties.getProperty("signing.store.password")
            keyAlias = localProperties.getProperty("signing.key.alias")
            keyPassword = localProperties.getProperty("signing.key.password")
        }
    }

    buildTypes {
        fun releaseAdId(propertyId: String) = localProperties.getProperty(propertyId)

        debug {
            buildConfigField(
                "String",
                "ADMOB_BANNER_SPLASH_ID",
                "\"${"ca-app-pub-3940256099942544/9214589741"}\""
            )
            buildConfigField(
                "String",
                "ADMOB_BANNER_HOME_ID",
                "\"${"ca-app-pub-3940256099942544/6300978111"}\""
            )
            buildConfigField(
                "String",
                "ADMOB_NATIVE_TODAY_ID",
                "\"${"ca-app-pub-3940256099942544/2247696110"}\""
            )
            buildConfigField(
                "String",
                "ADMOB_NATIVE_CALENDAR_ID",
                "\"${"ca-app-pub-3940256099942544/2247696110"}\""
            )
            buildConfigField(
                "String",
                "ADMOB_INTER_HOME_ID",
                "\"${"ca-app-pub-3940256099942544/1033173712"}\""
            )
            buildConfigField(
                "String",
                "ADMOB_INTER_SPLASH_ID",
                "\"${"ca-app-pub-3940256099942544/1033173712"}\""
            )
            buildConfigField(
                "String",
                "ADMOB_REWARD_WIDGET_ID",
                "\"${"ca-app-pub-3940256099942544/5224354917"}\""
            )
        }

        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "ADMOB_BANNER_SPLASH_ID",
                "\"${releaseAdId("admob.banner.splash.id")}\""
            )
            buildConfigField(
                "String",
                "ADMOB_BANNER_HOME_ID",
                "\"${releaseAdId("admob.banner.home.id")}\""
            )
            buildConfigField(
                "String",
                "ADMOB_NATIVE_TODAY_ID",
                "\"${releaseAdId("admob.native.today.id")}\""
            )
            buildConfigField(
                "String",
                "ADMOB_NATIVE_CALENDAR_ID",
                "\"${releaseAdId("admob.native.calendar.id")}\""
            )
            buildConfigField(
                "String",
                "ADMOB_INTER_HOME_ID",
                "\"${releaseAdId("admob.inter.home.id")}\""
            )
            buildConfigField(
                "String",
                "ADMOB_INTER_SPLASH_ID",
                "\"${releaseAdId("admob.inter.splash.id")}\""
            )
            buildConfigField(
                "String",
                "ADMOB_REWARD_WIDGET_ID",
                "\"${releaseAdId("admob.reward.widget.id")}\""
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        aidl = false
        buildConfig = true
        shaders = false
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Arch Components
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    // Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Instrumented tests
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Instrumented tests: jUnit rules and runners
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Navigation
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Icons Extended
    implementation(libs.androidx.compose.material.icons.extended)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Timber
    implementation(libs.timber)

    // Immutable Collections
    implementation(libs.kotlinx.collections.immutable)

    // Glance Widget
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.startup.runtime)

    // AdMob
    implementation(libs.play.services.ads)


    // Lottie
    implementation(libs.lottie.compose)

    // ConstraintLayout
    implementation(libs.androidx.constraintlayout)
}
