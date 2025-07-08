import 'package:get/get.dart';
import '../pages/login_page.dart'; // 修改导入路径
import '../pages/current_location_page.dart'; // 添加必要导入

class AppRoutes {
  static const String LOGIN = '/login';
  static const String HOME = '/home';

  static final routes = [
    GetPage(name: LOGIN, page: () => LoginPage()),
    GetPage(name: HOME, page: () => CurrentLocationPage()), // 临时使用现有页面
  ];
}