

keytool -genkey -v -keystore release-keystore.jks -storetype JKS -keyalg RSA -keysize 2048 -validity 10000 -alias release

keytool -genkey -v -keystore debug-keystore.jks -storetype JKS -keyalg RSA -keysize 2048 -validity 10000 -alias debug


keytool -list -v -keystore android/app/release-keystore.jks -alias release
keytool -list -v -keystore android/app/debug-keystore.jks -alias debug


