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
      title: '设置',
      body: ListView(
        children: [
          // 语言设置
          ListTile(
            title: Text('语言设置'),
            subtitle: Text(
              '当前语言: ${Get.locale?.languageCode == 'en' ? '英文' : '中文'}',
            ),
            trailing: DropdownButton<String>(
              value: Get.locale?.languageCode,
              items: const [
                DropdownMenuItem(value: 'zh', child: Text('中文')),
                DropdownMenuItem(value: 'en', child: Text('英文')),
              ],
              onChanged: (value) async {
                if (value != null) {
                  LogUtil.info('language: $value');
                  Get.updateLocale(Locale(value));
                  await _prefs.setString(
                    PrefsConstants.language,
                    value,
                  ); // 保存到SharedPreferences
                }
              },
            ),
          ),

          // 主题设置
          ListTile(
            title: Text('主题颜色'),
            subtitle: Text(
              '当前主题: ${_getThemeName(_themeController.currentTheme.value)}',
            ),
            trailing: DropdownButton<ThemeData>(
              value: _themeController.currentTheme.value,
              items: [
                DropdownMenuItem(
                  value: Themes.light,
                  child: Row(
                    children: [
                      Container(width: 16, height: 16, color: Colors.blue),
                      SizedBox(width: 8),
                      Text('浅色'),
                    ],
                  ),
                ),
                DropdownMenuItem(
                  value: Themes.dark,
                  child: Row(
                    children: [
                      Container(width: 16, height: 16, color: Colors.grey),
                      SizedBox(width: 8),
                      Text('深色'),
                    ],
                  ),
                ),
                // DropdownMenuItem(
                //   value: Themes.system(context),
                //   child: Row(
                //     children: [
                //       Icon(Icons.settings_system_daydream),
                //       SizedBox(width: 8),
                //       Text('跟随系统'),
                //     ],
                //   ),
                // ),
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
                  ); // 保存到SharedPreferences
                }
              },
            ),
          ),

          // 环境设置
          ListTile(
            title: Text('环境设置'),
            subtitle: Obx(
              () =>
                  Text('当前环境: ${_getEnvName(_envController.currentEnv.value)}'),
            ),
            trailing: DropdownButton<String>(
              value: _envController.currentEnv.value,
              items: const [
                DropdownMenuItem(value: 'prod', child: Text('生产环境')),
                DropdownMenuItem(value: 'dev', child: Text('开发环境')),
                DropdownMenuItem(value: 'mock', child: Text('Mock环境')),
              ],
              onChanged: (value) async {
                if (value != null) {
                  LogUtil.info('env: $value');
                  _envController.changeEnv(value);
                  await _prefs.setString(
                    PrefsConstants.env,
                    value,
                  ); // 保存到SharedPreferences
                }
              },
            ),
          ),

          ListTile(
            title: Text('检查更新'),
            subtitle: Text('检查是否有新版本可用'),
            trailing: Icon(Icons.system_update),
            onTap: _checkForUpdates,
          ),
          // 清空缓存选项
          ListTile(
            title: Text('清空缓存'),
            subtitle: Text('清除所有本地存储的数据'),
            trailing: Icon(Icons.delete),
            onTap: () async {
              final confirmed = await Get.dialog<bool>(
                AlertDialog(
                  title: Text('确认'),
                  content: Text('确定要清空所有缓存数据吗？此操作不可撤销。'),
                  actions: [
                    TextButton(
                      onPressed: () => Get.back(result: false),
                      child: Text('取消'),
                    ),
                    TextButton(
                      onPressed: () => Get.back(result: true),
                      child: Text('确定'),
                    ),
                  ],
                ),
              );

              if (confirmed == true) {
                await PrefsUtil.clearAll();
                Get.snackbar('成功', '缓存已清空');
              }
            },
          ),
          // 新增退出应用按钮
          ListTile(
            title: Text('退出应用'),
            subtitle: Text('退出应用程序'),
            trailing: Icon(Icons.exit_to_app),
            onTap: () {
              Get.dialog(
                AlertDialog(
                  title: Text('确认退出'),
                  content: Text('确定要退出应用吗？'),
                  actions: [
                    TextButton(onPressed: () => Get.back(), child: Text('取消')),
                    TextButton(
                      onPressed: () => SystemNavigator.pop(),
                      child: Text('确定'),
                    ),
                  ],
                ),
              );
            },
          ),
          // 新增退出登录按钮
          ListTile(
            title: Text('退出登录'),
            subtitle: Text('清除登录信息并返回登录页面'),
            trailing: Icon(Icons.logout),
            onTap: () {
              Get.dialog(
                AlertDialog(
                  title: Text('确认退出登录'),
                  content: Text('确定要退出登录吗？'),
                  actions: [
                    TextButton(onPressed: () => Get.back(), child: Text('取消')),
                    TextButton(
                      onPressed: () async {
                        // 清除登录信息
                        await PrefsUtil.clearAll();
                        // 跳转到登录页面
                        Get.offAllNamed(AppRoutes.LOGIN);
                      },
                      child: Text('确定'),
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
    if (theme == Themes.light) return '浅色';
    if (theme == Themes.dark) return '深色';
    return '默认';
  }

  String _getEnvName(String env) {
    switch (env) {
      case 'prod':
        return '生产环境';
      case 'dev':
        return '开发环境';
      case 'mock':
        return 'Mock环境';
      default:
        return '未知环境';
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
        throw Exception(response['message'] ?? '检查更新失败');
      }

      final data = response['data'];
      if (data['needUpdate'] == true) {
        final versionInfo = data['version'];

        // 显示更新对话框
        Get.dialog(
          AlertDialog(
            title: Text('发现新版本 ${versionInfo['version']}'),
            content: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('发布日期: ${versionInfo['releaseDate']}'),
                SizedBox(height: 8),
                Text('更新内容:'),
                Text(versionInfo['releaseNotes']),
                if (versionInfo['forceUpdate'])
                  Padding(
                    padding: EdgeInsets.only(top: 8),
                    child: Text(
                      '* 此版本必须更新',
                      style: TextStyle(color: Colors.red),
                    ),
                  ),
              ],
            ),
            actions: [
              if (!versionInfo['forceUpdate'])
                TextButton(onPressed: () => Get.back(), child: Text('取消')),
              TextButton(
                onPressed: () {
                  Get.back();
                  _startUpdate(versionInfo['version']);
                },
                child: Text('更新'),
              ),
            ],
          ),
        );
      } else {
        Get.snackbar('提示', '当前已是最新版本');
      }
    } catch (e) {
      Get.snackbar('错误', '检查更新失败: ${e.toString()}');
    }
  }

  void _startUpdate(String version) {
    // 跳转到更新页面
    Get.offNamed(AppRoutes.UPDATE, arguments: {'version': version});
  }
}
