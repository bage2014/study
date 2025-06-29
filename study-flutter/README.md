## Flutter 入门

### 环境搭建

https://docs.flutter.cn/community/china/

下载

https://docs.flutter.cn/release/archive?tab=macos

配置

```
export PUB_HOSTED_URL="https://pub.flutter-io.cn"
export FLUTTER_STORAGE_BASE_URL="https://storage.flutter-io.cn"
```

创建目录

```
mkdir ~/bage/software/flutter2025/

cd ~/bage/software/flutter2025/bin

```

配置验证

```
export PATH="$PWD/flutter2025/bin:$PATH"

flutter doctor



export EMULATOR_HOME=/Users/bage/Library/Android/sdk/emulator
export PATH=$EMULATOR_HOME:$PATH
```



常用命令

```
D:\softwares\SDK\emulator\emulator -avd Pixel2XLAPI29 -dns-server 10.0.3.2

flutter build apk --no-sound-null-safety

```



## Flutter 入门

http://olive.ren/flutter-build-apk-fail/

https://developer.android.google.cn/studio/run/managing-avds#createavd

https://fluttergems.dev/

https://github.com/jahnli/awesome-flutter-plugins

### TODO

服务端代码改造

网络提示,请先连接网络 

同意提示，多语言问题
下载进度条（参考 https://github.com/rhymelph/r_upgrade/blob/v0.3.2/README_CN.md）

分享

家庭族谱

足迹记录-地图

大厂内推APP

https://github.com/lohanidamodar/flutter_ui_challenges



https://github.com/FlutterOpen/flutter-ui-nice





小组活动：每周体验一款app，产品包括最近热门，评价高，国外好的产品。建议从产品、市场两方面发表看法，也可以自由交流~
产品方面：
1.主要功能
2.解决用户什么需求
3.独特卖点
4.关键指标

市场方面：
1.用户群
2.推广渠道
3.收入分析
4.成本分析



app idea reference:

https://www.douban.com/group/689011/

https://www.douban.com/group/705002/

https://www.douban.com/group/gameres/

https://www.douban.com/group/523351/


### 参考链接

https://flutter.dev/docs/get-started/install/windows

https://github.com/befovy/fijkplayer/ 开源的视频播放器

开源APP 汇总

https://blog.csdn.net/weixin_38627381/article/details/79037500

https://www.jianshu.com/p/d92258d76f7e

Froid APP爬虫



https://github.com/mitesh77/Best-Flutter-UI-Templates

https://github.com/iampawan/Flutter-UI-Kit

https://github.com/olayemii/flutter-ui-kits

https://github.com/flutter/gallery/tree/master/lib


https://f-droid.org/

app 列表抓取

个人设置

游客身份



https://github.com/Solido/awesome-flutter

https://github.com/robertodevs/flutter_ecommerce_template

http://laomengit.com/guide/intl/%E6%B7%BB%E5%8A%A0%E5%9B%BD%E9%99%85%E5%8C%96%E6%94%AF%E6%8C%81.html





下载安装 Android studio

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

flutter doctor ???

flutter_app_upgrade 插件使用问题

ServiceSpecificException: (code -22)

新建文件 \android\app\src\main\res\xml\file_paths.xml

flutter build apk 报错：
http://olive.ren/flutter-build-apk-fail/



https://stackoverflow.com/questions/54552962/flutter-build-error-process-command-e-flutter-apps-flutter-bin-flutter-bat

You can try `reconfiguring the Flutter SDK path` in Android Studio's `File | Settings | Languages & Frameworks | Flutter`.

I have just encountered the same problem and solved it successfully with this method.



https://stackoverflow.com/questions/64917744/cannot-run-with-sound-null-safety-because-dependencies-dont-support-null-safety

In Android Studio:

```
Run --> Edit Configurations --> Add Additional Run args --> --no-sound-null-safety
```



```
flutter build apk --no-sound-null-safety
```



Flutter 基本概念

组件

网络

JSON

资源

布局

多语言 Flutter Intl

家庭族谱，小工具、实用工具

个人名片、联系方式

C:\Users\bage\.gradle\gradle.properties
移除

