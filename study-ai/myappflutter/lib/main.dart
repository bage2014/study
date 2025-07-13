import 'package:flutter/material.dart';
import 'package:myappflutter/config/themes.dart';
import 'package:myappflutter/pages/home_page.dart';
import 'package:get/get.dart';
import 'package:myappflutter/common/constants.dart';
import 'config/app_routes.dart';
import 'config/themes.dart';
import 'lang/translation.dart';
import 'controllers/auth_controller.dart';
import 'controllers/theme_controller.dart';
import 'controllers/env_controller.dart'; // 添加导入
import 'controllers/theme_controller.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // 初始化GetX控制器
  Get.put(ThemeController());
  Get.put(AuthController());
  Get.put(EnvController());

  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: '位置追踪应用',
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
