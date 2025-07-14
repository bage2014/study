import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'core/config/app_routes.dart';
import 'core/theme/themes.dart';
import 'localization/translation.dart';
import 'features/controller/auth_controller.dart';
import 'features/controller/theme_controller.dart';
import 'features/controller/env_controller.dart';

void main() {
  // 注册HttpClient和EnvController
  Get.put(EnvController());
  Get.put(AuthController());
  Get.put(ThemeController());

  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: '我的应用',
      initialRoute: AppRoutes.LOGIN, // 应用启动直接进入登录页面
      getPages: AppRoutes.routes,
      theme: Themes.light,
      darkTheme: Themes.dark,
      translations: MyTranslations(),
      locale: const Locale('zh', 'CN'),
      fallbackLocale: const Locale('en', 'US'),
    );
  }
}
