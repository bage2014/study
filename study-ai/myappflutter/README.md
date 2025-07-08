# myappflutter

A new Flutter project.

## Getting Started

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://docs.flutter.dev/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://docs.flutter.dev/cookbook)

For help getting started with Flutter development, view the
[online documentation](https://docs.flutter.dev/), which offers tutorials,
samples, guidance on mobile development, and a full API reference.




keytool -genkey -v -keystore ~/upload-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload

keytool -list -v -keystore ~/upload-keystore.jks -alias upload





lib/
├── api/                 # 网络请求层
│   ├── api_service.dart  # API服务封装
│   └── http_client.dart  # GetConnect配置
├── common/              # 公共资源
│   ├── constants.dart    # 常量定义
│   ├── utils.dart        # 工具函数
│   └── widgets/          # 通用组件
│       ├── custom_dialog.dart
│       └── search_box.dart
├── config/              # 应用配置
│   ├── app_routes.dart   # 路由配置
│   └── themes.dart       # 主题配置
├── controllers/         # 状态管理
│   ├── auth_controller.dart
│   ├── theme_controller.dart
│   └── locale_controller.dart
├── lang/                # 国际化资源
│   ├── en.dart
│   ├── zh.dart
│   └── translation.dart
├── models/              # 数据模型
│   └── user_model.dart
├── views/               # 页面视图
│   ├── login/
│   │   └── login_page.dart
│   ├── home/
│   └── settings/
└── main.dart            # 应用入口