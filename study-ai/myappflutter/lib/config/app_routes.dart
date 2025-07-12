import 'package:get/get.dart';
import '../pages/login_page.dart';
import '../pages/home_page.dart';
import '../pages/current_location_page.dart';
import '../pages/find_location_page.dart';
import '../pages/history_location_page.dart';
import '../pages/track_location_page.dart';
import '../pages/settings_page.dart'; // 添加设置页面导入
import '../pages/family_page.dart'; // 添加家庭关系页面导入

class AppRoutes {
  static const String LOGIN = '/login';
  static const String HOME = '/home';
  static const String CURRENT_LOCATION = '/current_location';
  static const String FIND_LOCATION = '/find_location';
  static const String HISTORY_LOCATION = '/history_location';
  static const String TRACK_LOCATION = '/track_location';
  static const String SETTINGS = '/settings'; // 添加设置页面路由
  static const String FAMILY = '/family'; // 添加家庭关系页面路由

  static final routes = [
    GetPage(name: LOGIN, page: () => const LoginPage()),
    GetPage(name: HOME, page: () => const HomePage()),
    GetPage(name: CURRENT_LOCATION, page: () => const CurrentLocationPage()),
    GetPage(name: FIND_LOCATION, page: () => const FindLocationPage()),
    GetPage(name: HISTORY_LOCATION, page: () => const HistoryLocationPage()),
    GetPage(name: TRACK_LOCATION, page: () => const TrackLocationPage()),
    GetPage(name: SETTINGS, page: () => SettingsPage()), // 移除const关键字
    GetPage(name: FAMILY, page: () => FamilyPage()),
  ];
}
