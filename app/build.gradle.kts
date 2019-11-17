import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    dataBinding {
        isEnabled = true
    }
    defaultConfig {
        applicationId = "com.google.samples.apps.sunflower"
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionCode = 1
        versionName = "0.1.6"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // work-runtime-ktx 2.1.0 and above now requires Java 8
    kotlinOptions {
        this as KotlinJvmOptions
        jvmTarget = "1.8"
    }
}

dependencies {
    kapt("androidx.room:room-compiler:${Versions.roomVersion}")
    kapt("com.github.bumptech.glide:compiler:${Versions.glideVersion}")
    implementation("androidx.appcompat:appcompat:${Versions.appCompatVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}")
    implementation("androidx.core:core-ktx:${Versions.ktxVersion}")
    implementation("androidx.fragment:fragment-ktx:${Versions.fragmentVersion}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerViewVersion}")
    implementation("androidx.room:room-runtime:${Versions.roomVersion}")
    implementation("androidx.room:room-ktx:${Versions.roomVersion}")
    implementation("androidx.viewpager2:viewpager2:${Versions.viewPagerVersion}")
    implementation("androidx.work:work-runtime-ktx:${Versions.workVersion}")
    implementation("com.github.bumptech.glide:glide:${Versions.glideVersion}")
    implementation("com.google.android.material:material:${Versions.materialVersion}")
    implementation("com.google.code.gson:gson:${Versions.gsonVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}")

    // Testing dependencies
    androidTestImplementation("androidx.arch.core:core-testing:${Versions.coreTestingVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Versions.espressoVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espressoVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-intents:${Versions.espressoVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.testExtJunit}")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:${Versions.uiAutomatorVersion}")
    androidTestImplementation("androidx.work:work-testing:${Versions.workVersion}")
    androidTestImplementation("com.google.truth:truth:${Versions.truthVersion}")
    testImplementation("junit:junit:${Versions.junitVersion}")
}
