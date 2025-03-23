import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.serialization)
    id("maven-publish")
    id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "io.github.anioutkazharkova"
version = "1.0.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
       // publishLibraryVariantsGroupedByFlavor = true
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    
    val xcf = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            xcf.add(this)
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.coroutines)
            implementation(libs.kotlinx.serialization.json)
        }
        androidMain.dependencies {
            implementation(libs.okhttp)
            implementation(libs.okhttp3.okhttp)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

// <module directory>/build.gradle.kts

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates("io.github.anioutkazharkova", "native_network_lib", "1.0.0")

    pom {
        name = "NativeLoaderLib"
        description = "Small Kotlin Multiplatform Library for native network"
        inceptionYear = "2025"
        url = "https://github.com/anioutkazharkova/native_network_lib"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "anioutkazharkova"
                name = "Anna Zharkova"
                url = "https://github.com/anioutkazharkova/"
            }
        }
        scm {
            url = "https://github.com/anioutkazharkova/native_network_lib"
            connection = "scm:git:git://anioutkazharkova/native_network_lib.git"
            developerConnection = "scm:git:ssh://git@github.com/anioutkazharkova/native_network_lib.git"
        }
    }
}

android {
    namespace = "ru.azharkova.nativeloaderlib"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
