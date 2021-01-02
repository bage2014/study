

## Flutter 入门



### 参考链接

https://flutter.dev/docs/get-started/install/windows

https://github.com/befovy/fijkplayer/ 开源的视频播放器

开源APP 汇总

https://blog.csdn.net/weixin_38627381/article/details/79037500

https://www.jianshu.com/p/d92258d76f7e

Froid APP爬虫



https://github.com/mitesh77/Best-Flutter-UI-Templates

https://github.com/iampawan/Flutter-UI-Kit

https://github.com/flutter/gallery/tree/master/lib


https://f-droid.org/



### 环境搭建

下载

Get the Flutter SDK

解压

配置Path 指向bin目录

下载安装 Android studio

配置flutter sdk

Android studio 配置flutter 出现“no devices”

flutter config --android-sdk D:\professional\sdk

```
#distributionUrl=https\://services.gradle.org/distributions/gradle-5.6.2-all.zip
distributionUrl=https\://services.gradle.org/distributions/gradle-6.1.1-all.zip
```



```
//        jcenter()
        maven{ url 'http://maven.aliyun.com/nexus/content/groups/public/'}
```



flutter pub get

打包

生成签名

```csharp
keytool -genkey -v -keystore ./sign.jks -keyalg RSA -keysize 2048 -validity 10000 -alias sign
```

导入签名

/android/sign.jks



build.gradle 添加

```csharp
def keystorePropertiesFile = rootProject.file("key.properties")
def keystoreProperties = new Properties()
keystoreProperties.load(new FileInputStream(keystorePropertiesFile))

android {
    signingConfigs {
        release {
            keyAlias 'sign'
            keyPassword 'android'
            storeFile file('key/sign.jks')
            storePassword 'android'
        }
    }

    buildTypes {
        release {
            // TODO: Add your own signing config for the release build.
            // Signing with the debug keys for now, so `flutter run --release` works.
            signingConfig signingConfigs.release
                
            ndk {
                //选择要添加的so库。
                abiFilters "armeabi", "armeabi-v7a" , "x86", "mips"//, "x86", "mips"
            }
        }
    }
}
```



运行命令生成APK

https://www.codercto.com/a/91681.html



```ruby
flutter build apk

flutter build apk --no-shrink
```



FLutter 入门

https://flutterchina.club/get-started/learn-more/

https://book.flutterchina.club/





Flutter 基本概念

组件

网络

JSON

资源

布局

多语言 Flutter Intl

家庭族谱，小工具、实用工具
