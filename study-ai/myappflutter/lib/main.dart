import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'core/config/app_routes.dart';
import 'core/theme/themes.dart';
import 'localization/translation.dart';
import 'features/controller/env_controller.dart';
import 'features/controller/auth_controller.dart';
import 'features/controller/theme_controller.dart';
import 'core/utils/app_init_util.dart';
import 'core/utils/prefs_util.dart';
import 'core/utils/log_util.dart';
import 'data/api/http_client.dart';
import 'core/constants/prefs_constants.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  // 注册控制器
  Get.put(EnvController());
  Get.put(AuthController());
  Get.put(ThemeController());
  await AppInitUtil.initialize();

  // 初始化百度地图 SDK
  // await BaiduMapSDK.init(ak: '你的百度地图AK密钥');

  // 调用抽取的方法获取初始路由
  final initialRoute = await _determineInitialRoute();
  runApp(MyApp(initialRoute: initialRoute));
}

// 抽取的自动登录检查方法
Future<String> _determineInitialRoute() async {
  // 默认路由为登录页
  String initialRoute = AppRoutes.LOGIN;
  final token = await PrefsUtil.getString(PrefsConstants.token);
  LogUtil.info('Main auto login check token = $token');

  if (token != null) {
    try {
      final httpClient = HttpClient();
      final response = await httpClient.post(
        '/checkToken',
        body: {'token': token},
      ).timeout(Duration(seconds: 1)); // 添加1秒超时设置

      if (response['code'] == 200) {
        initialRoute = AppRoutes.HOME;
        // 更新AuthController登录状态
        Get.find<AuthController>().login();
      } else {
        // 令牌无效，清除本地存储
        await PrefsUtil.remove(PrefsConstants.token);
        await PrefsUtil.remove(PrefsConstants.userInfo);
        await PrefsUtil.remove(PrefsConstants.tokenExpireTime);
      }
    } catch (e) {
      // 这里会捕获超时异常以及其他异常
      LogUtil.error('Auto login check token failed: $e');
    }
  }
  return initialRoute;
}

class MyApp extends StatelessWidget {
  final String initialRoute;
  const MyApp({super.key, required this.initialRoute});

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: '我的应用',
      initialRoute: initialRoute, // 使用动态确定的初始路由
      getPages: AppRoutes.routes,
      theme: Themes.light,
      darkTheme: Themes.dark,
      translations: MyTranslations(),
      locale: const Locale('zh', 'CN'),
      fallbackLocale: const Locale('en', 'US'),
    );
  }
}
