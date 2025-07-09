import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'config/app_routes.dart';
import 'config/themes.dart';
import 'lang/translation.dart';
import 'controllers/auth_controller.dart';
import 'controllers/theme_controller.dart';
import 'controllers/env_controller.dart'; // 添加导入

void main() {
  Get.put(AuthController());
  Get.put(ThemeController());
  Get.put(EnvController()); // 初始化环境控制器
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: '位置追踪应用',
      initialRoute: AppRoutes.LOGIN, // 应用启动直接进入登录页面
      getPages: AppRoutes.routes,
      theme: Themes.lightBlue,
      darkTheme: Themes.dark,
      translations: MyTranslations(),
      locale: const Locale('zh', 'CN'),
      fallbackLocale: const Locale('en', 'US'),
    );
  }
}
