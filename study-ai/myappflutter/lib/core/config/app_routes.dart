import 'package:get/get.dart';
import 'package:myappflutter/presentation/pages/family_edit_page.dart';
import 'package:myappflutter/presentation/pages/tv_group_channels_page.dart';
import 'package:myappflutter/presentation/pages/user_search_page.dart';
import '../../presentation/pages/login_page.dart';
import '../../presentation/pages/home_page.dart';
import '../../presentation/pages/current_location_page.dart';
import '../../presentation/pages/history_location_page.dart';
import '../../presentation/pages/track_location_page.dart';
import '../../presentation/pages/settings_page.dart'; // 添加设置页面导入
import '../../presentation/pages/family_page.dart'; // 添加家庭关系页面导入
import '../../presentation/pages/profile_page.dart'; // 新增导入
import '../../presentation/pages/update_page.dart'; // 新增导入
import '../../presentation/pages/message_page.dart';
import '../../presentation/pages/register_page.dart';
import '../../presentation/pages/tv_player_page.dart';
import '../../presentation/pages/tv_list_page.dart'; // 新增导入
import '../../presentation/pages/live_all_page.dart'; // 新增导入
import '../../presentation/pages/live_group_page.dart'; // 新增导入
import '../../presentation/pages/app_version_page.dart'; // 新增应用版本页面导入
import '../../presentation/pages/ai_page.dart'; // 新增AI页面导入
import '../../presentation/pages/file_upload_page.dart'; // 新增文件上传页面导入

class AppRoutes {
  static const String LOGIN = '/login';
  static const String HOME = '/home';
  static const String CURRENT_LOCATION = '/current_location';
  static const String FIND_LOCATION = '/find_location';
  static const String HISTORY_LOCATION = '/history_location';
  static const String TRACK_LOCATION = '/track_location';
  static const String SETTINGS = '/settings'; // 添加设置页面路由
  static const String FAMILY = '/family'; // 添加家庭关系页面路由
  static const String FAMILY_EDIT = '/family_edit'; // 添加家庭关系编辑页面路由
  static const String PROFILE = '/profile';
  static const String UPDATE = '/update';
  static const String MESSAGE = '/message';
  // 在 app_routes.dart 中添加
  static const String REGISTER = '/register';
  static const String TV_PLAYER = '/tv_player'; // 定义TV播放器路由
  static const String TV_LIST = '/tv_list'; // 新增路由常量
  static const String USER_SEARCH = '/user/search';
  static const String LIVE = '/live'; // 新增直播页面路由
  static const String LIVE_ALL = '/live_all'; // 新增全部频道页面路由
  static const String LIVE_GROUP = '/live_group'; // 新增频道分组页面路由
  static const String LIVE_CHANNEL_GROUP = '/live_channel_group'; // 新增频道分组页面路由
  static const String APP_VERSION = '/app_version'; // 新增应用版本页面路由
  static const String AI = '/ai'; // AI页面路由
  static const String FILE_UPLOAD = '/file_upload'; // 文件上传页面路由

  static final routes = [
    GetPage(name: LOGIN, page: () => const LoginPage()),
    GetPage(name: HOME, page: () => HomePage()), // 移除 const 关键字
    GetPage(name: CURRENT_LOCATION, page: () => const CurrentLocationPage()),
    GetPage(name: HISTORY_LOCATION, page: () => const HistoryLocationPage()),
    GetPage(name: TRACK_LOCATION, page: () => const TrackLocationPage()),
    GetPage(name: SETTINGS, page: () => SettingsPage()), // 移除const关键字
    GetPage(name: FAMILY, page: () => FamilyPage()),
    GetPage(name: FAMILY_EDIT, page: () => FamilyEditPage()),
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
    GetPage(name: USER_SEARCH, page: () => const UserSearchPage()),

    // 修改路由定义
    GetPage(
      name: TV_PLAYER,
      page: () {
        // 提供默认的 m3u8 直播流 URL 作为参数
        return TvPlayerPage(
          channel: Get.arguments,
          streamUrl:
              'https://stream-akamai.castr.com/5b9352dbda7b8c769937e459/live_2361c920455111ea85db6911fe397b9e/index.fmp4.m3u8',
        );
      },
    ),
    GetPage(
      name: TV_LIST,
      page: () => const TvListPage(),
      transition: Transition.fadeIn,
    ),

    GetPage(
      name: LIVE_ALL,
      page: () => const LiveAllPage(),
      transition: Transition.fadeIn,
    ),
    GetPage(
      name: LIVE_GROUP,
      page: () => const LiveGroupPage(),
      transition: Transition.fadeIn,
    ),
    GetPage(
      name: LIVE_CHANNEL_GROUP,
      page: () {
        final args = Get.arguments;
        return TVGroupChannelPage(categoryName: args['categoryName']);
      },
      transition: Transition.fadeIn,
    ),
    GetPage(
      name: APP_VERSION,
      page: () => const AppVersionPage(),
      transition: Transition.fadeIn,
    ),
    GetPage(
      name: AppRoutes.AI,
      page: () => const AiPage(),
      transition: Transition.fadeIn,
    ),
    GetPage(
      name: AppRoutes.FILE_UPLOAD,
      page: () => const FileUploadPage(),
      transition: Transition.fadeIn,
    ),
  ];
}
