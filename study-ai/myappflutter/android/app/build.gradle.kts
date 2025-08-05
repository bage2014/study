plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.bage.my.app.flutter"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = "27.2.12479018"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.bage.my.app.flutter"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        // minSdk = flutter.minSdkVersion
        minSdk = 24
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    signingConfigs {
        create("release") {
            storeFile = file("upload-keystore.jks")
            storePassword = "myappflutter"
            keyAlias = "upload"
            keyPassword = "myappflutter"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }

}

dependencies {
    implementation 'com.baidu.lbsyun:BaiduMapSDK_Map:7.5.4'
    implementation 'com.baidu.lbsyun:BaiduMapSDK_Search:7.5.4'
    implementation 'com.baidu.lbsyun:BaiduMapSDK_Util:7.5.4'
}
// 添加百度地图SDK依赖
//  dependencies {
//     implementation("com.baidu.mapapi:base:7.4.0")
//     implementation("com.baidu.mapapi:map:7.4.0")
//     implementation("com.baidu.mapapi:location:7.4.0")
// }

flutter {
    source = "../.."
}

