import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../widgets/base_page.dart';
import '../../core/theme/themes.dart';
import '../../features/controller/theme_controller.dart';
import '../../features/controller/env_controller.dart';
import '../../core/utils/prefs_util.dart';
import '../../data/api/http_client.dart';
import '../../core/config/app_routes.dart';
import 'package:flutter/services.dart';
import '../../core/constants/prefs_constants.dart';

class SettingsPage extends StatefulWidget {
  const SettingsPage({super.key});

  @override
  State<SettingsPage> createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  final ThemeController _themeController = Get.find<ThemeController>();
  final EnvController _envController = Get.find<EnvController>();
  late SharedPreferences _prefs;

  @override
  void initState() {
    super.initState();
    _initPreferences();
  }

  Future<void> _initPreferences() async {
    _prefs = await SharedPreferences.getInstance();
    // 从SharedPreferences加载设置
    _loadSettings();
  }

  void _loadSettings() {
    // 语言设置
    final language = _prefs.getString('language');
    LogUtil.info('language: $language');
    if (language != null) {
      Get.updateLocale(Locale(language));
    }

    // 主题设置
    final theme = _prefs.getString('theme');
    LogUtil.info('theme: $theme');
    if (theme == 'light') {
      _themeController.changeTheme(Themes.light);
    } else if (theme == 'dark') {
      _themeController.changeTheme(Themes.dark);
    }

    // 环境设置
    final env = _prefs.getString('env');
    LogUtil.info('env: $env');
    if (env != null) {
      _envController.changeEnv(env);
    }
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'settings'.tr,
      body: ListView(
        children: [
          // 语言设置
          // 语言设置
          ListTile(
            title: Text('language_settings'.tr),
            subtitle: Text(
              '${'current_language'.tr}: ${Get.locale?.languageCode == 'en' ? 'english'.tr : 'chinese'.tr}',
            ),
            trailing: DropdownButton<String>(
              value: Get.locale?.languageCode,
              items: [
                DropdownMenuItem(value: 'zh', child: Text('chinese'.tr)),
                DropdownMenuItem(value: 'en', child: Text('english'.tr)),
              ],
              onChanged: (value) async {
                if (value != null) {
                  LogUtil.info('language: $value');
                  Get.updateLocale(Locale(value));
                  await _prefs.setString(
                    PrefsConstants.language,
                    value,
                  );
                }
              },
            ),
          ),

          // 主题设置
          ListTile(
            title: Text('theme_color'.tr),
            subtitle: Text(
              '${'current_theme'.tr}: ${_getThemeName(_themeController.currentTheme.value)}',
            ),
            trailing: DropdownButton<ThemeData>(
              value: _themeController.currentTheme.value,
              items: [
                DropdownMenuItem(
                  value: Themes.light,
                  child: Row(
                    children: [
                      Container(width: 16, height: 16, color: Colors.white),
                      SizedBox(width: 8),
                      Text('light'.tr),
                    ],
                  ),
                ),
                DropdownMenuItem(
                  value: Themes.dark,
                  child: Row(
                    children: [
                      Container(width: 16, height: 16, color: Colors.blue),
                      SizedBox(width: 8),
                      Text('dark'.tr),
                    ],
                  ),
                ),
              ],
              onChanged: (value) async {
                if (value != null) {
                  String theme;
                  if (value == Themes.light) {
                    theme = 'light';
                  } else if (value == Themes.dark) {
                    theme = 'dark';
                  } else {
                    theme = 'system';
                  }
                  LogUtil.info('theme: $theme.');
                  _themeController.changeTheme(value);
                  await _prefs.setString(
                    'theme',
                    theme,
                  );
                }
              },
            ),
          ),

          // 环境设置
          ListTile(
            title: Text('env_settings'.tr),
            subtitle: Obx(
              () =>
                  Text('${'current_env'.tr}: ${_getEnvName(_envController.currentEnv.value)}'),
            ),
            trailing: DropdownButton<String>(
              value: _envController.currentEnv.value,
              items: [
                DropdownMenuItem(value: 'prod', child: Text('prod'.tr)),
                DropdownMenuItem(value: 'dev', child: Text('dev'.tr)),
                DropdownMenuItem(value: 'mock', child: Text('mock'.tr)),
              ],
              onChanged: (value) async {
                if (value != null) {
                  LogUtil.info('env: $value');
                  _envController.changeEnv(value);
                  await _prefs.setString(
                    PrefsConstants.env,
                    value,
                  );
                }
              },
            ),
          ),

          ListTile(
            title: Text('check_updates'.tr),
            subtitle: Text('check_new_version'.tr),
            trailing: Icon(Icons.system_update),
            onTap: _checkForUpdates,
          ),
          // 消息页面选项
          ListTile(
            title: Text('message'.tr),
            subtitle: Text('view_messages'.tr),
            trailing: Icon(Icons.message),
            onTap: () {
              Get.toNamed(AppRoutes.MESSAGE);
            },
          ),
          // 清空缓存选项
          ListTile(
            title: Text('clear_cache'.tr),
            subtitle: Text('clear_all_data'.tr),
            trailing: Icon(Icons.delete),
            onTap: () async {
              final confirmed = await Get.dialog<bool>(
                AlertDialog(
                  title: Text('confirm'.tr),
                  content: Text('confirm_clear_cache'.tr),
                  actions: [
                    TextButton(
                      onPressed: () => Get.back(result: false),
                      child: Text('cancel'.tr),
                    ),
                    TextButton(
                      onPressed: () => Get.back(result: true),
                      child: Text('confirm'.tr),
                    ),
                  ],
                ),
              );

              if (confirmed == true) {
                await PrefsUtil.clearAll();
                Get.snackbar('success'.tr, 'cache_cleared'.tr);
              }
            },
          ),
          // 新增退出应用按钮
          ListTile(
            title: Text('exit_app'.tr),
            subtitle: Text('exit_application'.tr),
            trailing: Icon(Icons.exit_to_app),
            onTap: () {
              Get.dialog(
                AlertDialog(
                  title: Text('confirm_exit'.tr),
                  content: Text('confirm_exit_app'.tr),
                  actions: [
                    TextButton(onPressed: () => Get.back(), child: Text('cancel'.tr)),
                    TextButton(
                      onPressed: () => SystemNavigator.pop(),
                      child: Text('confirm'.tr),
                    ),
                  ],
                ),
              );
            },
          ),
          // 新增退出登录按钮
          ListTile(
            title: Text('logout'.tr),
            subtitle: Text('clear_login_info_and_return_to_login_page'.tr),
            trailing: Icon(Icons.logout),
            onTap: () {
              Get.dialog(
                AlertDialog(
                  title: Text('confirm_logout'.tr),
                  content: Text('confirm_logout_msg'.tr),
                  actions: [
                    TextButton(onPressed: () => Get.back(), child: Text('cancel'.tr)),
                    TextButton(
                      onPressed: () async {
                        // 清除登录信息
                        await PrefsUtil.clearAll();
                        // 跳转到登录页面
                        Get.offAllNamed(AppRoutes.LOGIN);
                      },
                      child: Text('confirm'.tr),
                    ),
                  ],
                ),
              );
            },
          ),
        ],
      ),
    );
  }

  String _getThemeName(ThemeData theme) {
    if (theme == Themes.light) return 'light'.tr;
    if (theme == Themes.dark) return 'dark'.tr;
    return 'default'.tr;
  }

  String _getEnvName(String env) {
    switch (env) {
      case 'prod':
        return 'prod_env'.tr;
      case 'dev':
        return 'dev_env'.tr;
      case 'mock':
        return 'mock_env'.tr;
      default:
        return 'unknown_environment'.tr;
    }
  }

  Future<void> _checkForUpdates() async {
    try {
      // 获取当前版本
      final currentVersion = 1;

      // 使用统一的HttpClient组件检查新版本
      final httpClient = HttpClient();
      final response = await httpClient.get(
        '/app/check?currentVersion=$currentVersion',
      );

      if (response['code'] != 200) {
        throw Exception(response['message'] ?? 'update_failed'.tr);
      }

      final data = response['data'];
      if (data['needUpdate'] == true) {
        final versionInfo = data['version'];

        // 显示更新对话框
        Get.dialog(
          AlertDialog(
            title: Text('${'found_new_version'.tr}${versionInfo['version']}'),
            content: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('${'release_date'.tr}${versionInfo['releaseDate']}'),
                SizedBox(height: 8),
                Text('${'update_content'.tr}'),
                Text(versionInfo['releaseNotes']),
                if (versionInfo['forceUpdate'])
                  Padding(
                    padding: EdgeInsets.only(top: 8),
                    child: Text(
                      '${'force_update'.tr}',
                      style: TextStyle(color: Colors.red),
                    ),
                  ),
              ],
            ),
            actions: [
              if (!versionInfo['forceUpdate'])
                TextButton(onPressed: () => Get.back(), child: Text('cancel'.tr)),
              TextButton(
                onPressed: () {
                  Get.back();
                  _startUpdate(versionInfo['version']);
                },
                child: Text('update'.tr),
              ),
            ],
          ),
        );
      } else {
        Get.snackbar('prompt'.tr, 'no_update_needed'.tr);
      }
    } catch (e) {
      Get.snackbar('error'.tr, '${'update_failed'.tr}${e.toString()}');
    }
  }

  void _startUpdate(String version) {
    // 跳转到更新页面
    Get.offNamed(AppRoutes.UPDATE, arguments: {'version': version});
  }
}
