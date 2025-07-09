import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../common/widgets/base_page.dart';
import '../config/themes.dart';
import '../controllers/theme_controller.dart';

class SettingsPage extends StatelessWidget {
  // 移除构造函数的const关键字
  SettingsPage({super.key});

  final ThemeController _themeController = Get.find<ThemeController>();

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: '设置',
      body: ListView(
        padding: const EdgeInsets.all(16),
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
}
