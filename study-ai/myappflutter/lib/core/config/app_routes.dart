import 'package:get/get.dart';
import '../../presentation/pages/login_page.dart';
import '../../presentation/pages/home_page.dart';
import '../../presentation/pages/current_location_page.dart';
import '../../presentation/pages/find_location_page.dart';
import '../../presentation/pages/history_location_page.dart';
import '../../presentation/pages/track_location_page.dart';
import '../../presentation/pages/settings_page.dart'; // 添加设置页面导入
import '../../presentation/pages/family_page.dart'; // 添加家庭关系页面导入
import '../../presentation/pages/profile_page.dart'; // 新增导入
import '../../presentation/pages/update_page.dart'; // 新增导入
import '../../presentation/pages/message_page.dart';
import '../../presentation/pages/register_page.dart';

class AppRoutes {
  static const String LOGIN = '/login';
  static const String HOME = '/home';
  static const String CURRENT_LOCATION = '/current_location';
  static const String FIND_LOCATION = '/find_location';
  static const String HISTORY_LOCATION = '/history_location';
  static const String TRACK_LOCATION = '/track_location';
  static const String SETTINGS = '/settings'; // 添加设置页面路由
  static const String FAMILY = '/family'; // 添加家庭关系页面路由
  static const String PROFILE = '/profile';
  static const String UPDATE = '/update';
  static const MESSAGE = '/message';
  // 在 app_routes.dart 中添加
  static const String REGISTER = '/register';

  static final routes = [
    GetPage(name: LOGIN, page: () => const LoginPage()),
    GetPage(name: HOME, page: () => HomePage()), // 移除 const 关键字
    GetPage(name: CURRENT_LOCATION, page: () => const CurrentLocationPage()),
    GetPage(name: FIND_LOCATION, page: () => const FindLocationPage()),
    GetPage(name: HISTORY_LOCATION, page: () => const HistoryLocationPage()),
    GetPage(name: TRACK_LOCATION, page: () => const TrackLocationPage()),
    GetPage(name: SETTINGS, page: () => SettingsPage()), // 移除const关键字
    GetPage(name: FAMILY, page: () => FamilyPage()),
    GetPage(name: PROFILE, page: () => const ProfilePage()),
    GetPage(
      name: UPDATE,
      page: () {
        final args = Get.arguments;
        return UpdatePage(version: args['version']);
      },
    ),
    GetPage(name: MESSAGE, page: () => const MessagePage()),
    GetPage(name: REGISTER, page: () => const RegisterPage()),
  ];
}
