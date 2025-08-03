import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../constants/prefs_constants.dart';
import '../theme/themes.dart';
import '../../features/controller/theme_controller.dart';
import '../../features/controller/env_controller.dart';
import 'log_util.dart';

class AppInitUtil {
  // 初始化应用设置
  static Future<void> initialize() async {
    LogUtil.info('开始初始化应用设置...');

    // 初始化SharedPreferences
    final prefs = await SharedPreferences.getInstance();

    // 初始化主题
    await _initializeTheme(prefs);

    // 初始化语言
    await _initializeLanguage(prefs);

    // 初始化环境
    await _initializeEnvironment(prefs);

    LogUtil.info('应用设置初始化完成');
  }

  // 初始化主题
  static Future<void> _initializeTheme(SharedPreferences prefs) async {
    final themeController = Get.put(ThemeController());
    final theme = prefs.getString(PrefsConstants.theme);

    LogUtil.info('初始化主题: $theme');

    if (theme == 'light') {
      themeController.changeTheme(Themes.light);
    } else if (theme == 'dark') {
      themeController.changeTheme(Themes.dark);
    } else {
      // 默认使用浅色主题
      themeController.changeTheme(Themes.light);
      await prefs.setString(PrefsConstants.theme, 'light');
    }
  }

  // 初始化语言
  static Future<void> _initializeLanguage(SharedPreferences prefs) async {
    final language = prefs.getString(PrefsConstants.language);

    LogUtil.info('初始化语言: $language');

    if (language != null) {
      await Get.updateLocale(Locale(language));
    } else {
      // 默认使用系统语言
      final systemLocale = Get.deviceLocale;
      if (systemLocale != null) {
        await Get.updateLocale(systemLocale);
        prefs.setString(PrefsConstants.language, systemLocale.languageCode);
      }
    }
  }

  // 初始化环境
  static Future<void> _initializeEnvironment(SharedPreferences prefs) async {
    final envController = Get.put(EnvController());
    final env = prefs.getString('env');

    LogUtil.info('初始化环境: $env');

    if (env != null) {
      envController.changeEnv(env);
    } else {
      // 默认使用生产环境
      envController.changeEnv('prod');
      prefs.setString('env', 'prod');
    }
  }
}
