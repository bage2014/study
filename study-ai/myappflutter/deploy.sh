

keytool -genkey -v -keystore release-keystore.jks -storetype JKS -keyalg RSA -keysize 2048 -validity 10000 -alias release

keytool -genkey -v -keystore debug-keystore.jks -storetype JKS -keyalg RSA -keysize 2048 -validity 10000 -alias debug

# 生成release密钥库
keytool -genkey -v -keystore android/app/release-keystore.jks -storetype JKS -keyalg RSA -keysize 2048 -validity 10000 -alias release -storepass myappflutter -keypass myappflutter -dname "CN=Your Name, OU=Your Organization, O=Your Company, L=Your City, ST=Your Province, C=CN"

# 生成debug密钥库
keytool -genkey -v -keystore android/app/debug-keystore.jks -storetype JKS -keyalg RSA -keysize 2048 -validity 10000 -alias debug -storepass myappflutter -keypass myappflutter -dname "CN=Your Name, OU=Your Organization, O=Your Company, L=Your City, ST=Your Province, C=CN"


keytool -list -v -keystore android/app/release-keystore.jks -alias release
keytool -list -v -keystore android/app/debug-keystore.jks -alias debug


