import 'package:get/get.dart';
import 'package:flutter/material.dart'; // 添加 ThemeData 所需导入
import '../../core/theme/themes.dart';

class ThemeController extends GetxController {
  final RxBool isDarkMode = false.obs;
  final Rx<ThemeData> currentTheme = Themes.light.obs;

  void changeTheme(ThemeData theme) {
    currentTheme.value = theme;
    Get.changeTheme(theme);
  }

  void toggleDarkMode() {
    isDarkMode.value = !isDarkMode.value;
    currentTheme.value = isDarkMode.value ? Themes.dark : Themes.light;
    Get.changeTheme(currentTheme.value);
  }
}
