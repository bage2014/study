// 添加必要的导入
import java.util.Properties
import java.io.FileInputStream

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

    // 读取local.properties文件中的百度地图API Key
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }
    val baiduMapApiKey = localProperties.getProperty("baidu_map_api_key") ?: ""

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

        // 添加百度地图API Key到manifest占位符
        manifestPlaceholders["baiduMapApiKey"] = baiduMapApiKey
    }

    // 加载签名配置
    

    signingConfigs {
        create("release") {
            storeFile = rootProject.file("app/release-keystore.jks")
            storePassword = "myappflutter"
            keyAlias = "release"
            keyPassword = "myappflutter"
        }
        getByName("debug") {
            storeFile = rootProject.file("app/debug.keystore")
            storePassword = "myappflutter"
            keyAlias = "debug"
            keyPassword = "myappflutter"
            // 修正属性名称
            isV1SigningEnabled = true
            isV2SigningEnabled = true
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
        release {
            signingConfig = signingConfigs.getByName("debug")
            signingConfig = signingConfigs.getByName("release")
        }
    }

}

// dependencies {
//     implementation ("com.baidu.lbsyun:BaiduMapSDK_Map:7.4.0")
//     implementation ("com.baidu.lbsyun:BaiduMapSDK_Search:7.4.0")
//     implementation ("com.baidu.lbsyun:BaiduMapSDK_Util:7.4.0")
// }
// 注释掉错误的依赖配置
// dependencies {
//    implementation("com.baidu.mapapi:base:7.4.0")
//    implementation("com.baidu.mapapi:map:7.4.0")
//    implementation("com.baidu.mapapi:location:7.4.0")
// }

// 添加正确的百度地图SDK依赖
 dependencies {
    implementation ("com.baidu.lbsyun:BaiduMapSDK_Map:7.4.0")
    implementation ("com.baidu.lbsyun:BaiduMapSDK_Search:7.4.0")
    implementation ("com.baidu.lbsyun:BaiduMapSDK_Util:7.4.0")
}

flutter {
    source = "../.."
}

