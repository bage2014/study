# tutorials

A tutorials project.

## Getting Started

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://flutter.dev/docs/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://flutter.dev/docs/cookbook)

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.


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


