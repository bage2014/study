# tutorials

A tutorials project.

## todo
登陆实效、1个月

今天吃什么！！

1、手动录入菜单？？

2、 随机选择？？ + 是否确定吃？

3、每月汇总？

4、 营养分析

个人名片
商用名片
家用名片

标签管理
名片管理


个人经典事迹？？ 别人点赞，验证真伪！！

我的音乐？视频？？啥的？？

自定义名片

文件上传

Mock 服务，，比如 10% 出现404

名片页面设计



2024年结束---上线APP

https://flutter.github.io/samples/



## 带源码APP

A collaborative list of open-source `iOS`, `iPadOS`, `watchOS`, `tvOS` and `visionOS` apps, your [contribution](https://github.com/dkhamsing/open-source-ios-apps/blob/master/.github/CONTRIBUTING.md) is welcome 😄

https://github.com/dkhamsing/open-source-ios-apps



## 参考资料

json 格式

https://javiercbk.github.io/json_to_dart/
https://jsontodart.com/

Flutter实战 https://book.flutterchina.club/

https://flutter.cn/

https://docs.flutter.dev/development/ui/widgets/material

官网常用
https://docs.flutter.dev/development/ui/widgets/material

带源码【组件 + 源码】
很多很赞
https://github.com/bukunmialuko/flutter_ui_kit_obkm

很多很赞
https://github.com/anoochit/uikits

很多很赞
https://github.com/lohanidamodar/flutter_ui_challenges

页面合集【也还可以】

https://github.com/FlutterOpen/flutter-ui-nice

常用页面
https://github.com/simplezhli/flutter_deer

应用集合
https://github.com/Vignesh0404/Flutter-UI-Kit

https://github.com/vinothvino42/SwiggyUI

https://github.com/usman18/Flutter-UI-Kit

组件
https://flutterui.design/

https://doc.flutterchina.club/widgets/



页面合集


https://fluttertemplates.dev/

https://github.com/iampawan/Flutter-UI-Kit

https://github.com/olayemii/flutter-ui-kits

https://www.behance.net/gallery/69411833/Backpack-UI-Kit-Free-for-Adobe-XD UI资源 很多



闹钟、通知类

https://pub.flutter-io.cn/packages/flutter_local_notifications

https://pub.flutter-io.cn/packages/awesome_notifications
https://pub.dev/packages/android_alarm_manager_plus


Flutter UI 

https://pub.flutter-io.cn/packages/fluent_ui/example
https://bdlukaa.github.io/fluent_ui/



UI 适配

https://pub.flutter-io.cn/packages/device_preview



代码简化？？

https://pub.flutter-io.cn/packages/freezed






FLutter Icon

https://www.fluttericon.cn/


生成签名
```csharp
keytool -genkey -v -keystore ./sign.jks -keyalg RSA -keysize 2048 -validity 10000 -alias sign
```

输入密码
bage.app

创建文件
key.properties
```csharp
storePassword=bage.app
keyPassword=bage.app
keyAlias=sign
storeFile=sign.jks
```

build.gradle
```csharp

def keystorePropertiesFile = rootProject.file("app/key/key.properties")
def keystoreProperties = new Properties()
keystoreProperties.load(new FileInputStream(keystorePropertiesFile))


android 节点下：

    signingConfigs {
            release {
                keyAlias 'sign'
                keyPassword 'bage.app'
                storeFile file('key/sign.jks')
                storePassword 'android'
            }
        }

        buildTypes {
            release {
                // TODO: Add your own signing config for the release build.
                // Signing with the debug keys for now, so `flutter run --release` works.
                signingConfig signingConfigs.release
                // signingConfig signingConfigs.debug

            }
        }

```



后端地址

10.0.2.2



账号：

bage2014@qq.com/ 123456
bage@qq.com/ 123456


studio 现有插件
dark. flutter  markdown

