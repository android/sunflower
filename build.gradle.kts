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

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
    }
}

plugins {
    id("com.diffplug.spotless") version "6.4.1"
}

//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//    }
//}

spotless {
    kotlin {
        target("**/*.kt")
        //ktlint(ktlintVersion).userData(["max_line_length": "100"])
    }
}

//subprojects {
//    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile).configureEach {
//        kotlinOptions {
//            jvmTarget = "1.8"
//            // Use experimental APIs
//            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
//        }
//    }
//}
