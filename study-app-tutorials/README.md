# tutorials

A tutorials project.

## todo
个人名片
商用名片
家用名片

个人经典事迹？？ 别人点赞，验证真伪！！

我的音乐？视频？？啥的？？

自定义名片

文件上传

Mock 服务，，比如 10% 出现404

名片页面设计

## 参考资料

https://jsonformatter.org/json-to-dart#Sample
https://jsontodart.com/
https://javiercbk.github.io/json_to_dart/

Flutter实战 https://book.flutterchina.club/

https://flutter.cn/

https://docs.flutter.dev/development/ui/widgets/material

带源码【组件 + 源码】
很多很赞
https://github.com/bukunmialuko/flutter_ui_kit_obkm

很多很赞
https://github.com/anoochit/uikits

很多很赞
https://github.com/lohanidamodar/flutter_ui_challenges

常用页面
https://github.com/simplezhli/flutter_deer

应用集合
https://github.com/Vignesh0404/Flutter-UI-Kit

https://github.com/vinothvino42/SwiggyUI

https://github.com/usman18/Flutter-UI-Kit



官网常用
https://docs.flutter.dev/development/ui/widgets/material

https://doc.flutterchina.club/widgets/



页面合集


https://fluttertemplates.dev/

https://github.com/iampawan/Flutter-UI-Kit

https://github.com/olayemii/flutter-ui-kits

https://www.behance.net/gallery/69411833/Backpack-UI-Kit-Free-for-Adobe-XD UI资源 很多



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


