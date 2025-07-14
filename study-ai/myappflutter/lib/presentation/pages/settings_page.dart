import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../widgets/base_page.dart';
import '../../core/theme/themes.dart';
import '../../features/controller/theme_controller.dart';
import '../../features/controller/env_controller.dart';

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
                    'language',
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
                      Container(width: 16, height: 16, color: Colors.green),
                      SizedBox(width: 8),
                      Text('深色'),
                    ],
                  ),
                ),
              ],
              onChanged: (value) async {
                if (value != null) {
                  final theme = value == Themes.light ? 'light' : 'dark';
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
                  await _prefs.setString('env', value); // 保存到SharedPreferences
                }
              },
            ),
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
}
