import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../common/widgets/base_page.dart';
import '../config/themes.dart';
import '../controllers/theme_controller.dart';
import '../controllers/env_controller.dart'; // 添加导入

class SettingsPage extends StatelessWidget {
  SettingsPage({super.key});

  final ThemeController _themeController = Get.find<ThemeController>();
  final EnvController _envController = Get.find<EnvController>(); // 添加环境控制器

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: '设置',
      body: ListView(
        padding: const EdgeInsets.all(64),
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
              onChanged: (value) {
                if (value != null) {
                  Get.updateLocale(Locale(value));
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
                  value: Themes.lightBlue,
                  child: Row(
                    children: [
                      Container(width: 16, height: 16, color: Colors.blue),
                      SizedBox(width: 8),
                      Text('蓝色'),
                    ],
                  ),
                ),
                DropdownMenuItem(
                  value: Themes.lightGreen,
                  child: Row(
                    children: [
                      Container(width: 16, height: 16, color: Colors.green),
                      SizedBox(width: 8),
                      Text('绿色'),
                    ],
                  ),
                ),
                DropdownMenuItem(
                  value: Themes.dark,
                  child: Row(
                    children: [
                      Container(width: 16, height: 16, color: Colors.grey[800]),
                      SizedBox(width: 8),
                      Text('深色'),
                    ],
                  ),
                ),
              ],
              onChanged: (value) {
                if (value != null) {
                  _themeController.changeTheme(value);
                }
              },
            ),
          ),

          // 环境设置 - 新增
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
              onChanged: (value) {
                if (value != null) {
                  _envController.changeEnv(value);
                }
              },
            ),
          ),
        ],
      ),
    );
  }

  String _getThemeName(ThemeData theme) {
    if (theme == Themes.lightBlue) return '蓝色';
    if (theme == Themes.lightGreen) return '绿色';
    if (theme == Themes.dark) return '深色';
    return '默认';
  }

  // 新增：获取环境显示名称
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
