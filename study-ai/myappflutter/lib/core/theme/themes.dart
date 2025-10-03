import 'package:flutter/material.dart';

class Themes {
  static final light = ThemeData(
    brightness: Brightness.light,
    // 使用现代、简洁的主色调
    primaryColor: const Color(0xFFFFFFFF),
    primaryColorLight: const Color(0xFFFFFFFF),
    primaryColorDark: const Color(0xFFFFFFFF),
    secondaryHeaderColor: const Color(0xFF4CAF50),
    // 以白色为主基调的背景色
    scaffoldBackgroundColor: Colors.white,
    // 添加canvasColor确保Container默认背景色为白色
    canvasColor: Colors.white,
    cardColor: Colors.white,
    // 优化文本主题，确保良好的可读性
    textTheme: TextTheme(
      displayLarge: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.bold,
        fontSize: 36,
      ),
      displayMedium: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.bold,
        fontSize: 28,
      ),
      displaySmall: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.bold,
        fontSize: 24,
      ),
      headlineLarge: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.bold,
        fontSize: 22,
      ),
      headlineMedium: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.bold,
        fontSize: 20,
      ),
      headlineSmall: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.bold,
        fontSize: 18,
      ),
      titleLarge: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.bold,
        fontSize: 18,
      ),
      titleMedium: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.w600,
        fontSize: 16,
      ),
      titleSmall: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.w600,
        fontSize: 14,
      ),
      bodyLarge: const TextStyle(color: Colors.black, fontSize: 16),
      bodyMedium: const TextStyle(color: Colors.black, fontSize: 14),
      bodySmall: TextStyle(color: Colors.black.withOpacity(0.7), fontSize: 12),
      labelLarge: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.w500,
        fontSize: 14,
      ),
      labelMedium: const TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.w500,
        fontSize: 12,
      ),
      labelSmall: TextStyle(
        color: Colors.black.withOpacity(0.7),
        fontWeight: FontWeight.w500,
        fontSize: 11,
      ),
    ),
    // 优化应用栏主题
    appBarTheme: AppBarTheme(
      color: Colors.white,
      elevation: 1,
      shadowColor: Colors.black.withOpacity(0.1),
      surfaceTintColor: Colors.white,
      titleTextStyle: const TextStyle(
        color: Colors.black,
        fontSize: 20,
        fontWeight: FontWeight.w600,
      ),
      iconTheme: const IconThemeData(color: Colors.black),
      actionsIconTheme: const IconThemeData(color: Colors.black),
    ),
    // 优化底部导航栏主题
    bottomNavigationBarTheme: const BottomNavigationBarThemeData(
      backgroundColor: Colors.white,
      elevation: 2,
      selectedItemColor: Color(0xFF3F51B5),
      unselectedItemColor: Colors.black54,
      selectedLabelStyle: TextStyle(fontSize: 12, fontWeight: FontWeight.w500),
      unselectedLabelStyle: TextStyle(fontSize: 12),
    ),
    // 优化颜色方案
    colorScheme: ColorScheme.light(
      primary: const Color(0xFF3F51B5),
      primaryContainer: const Color(0xFFE8EAF6),
      secondary: const Color(0xFF4CAF50),
      secondaryContainer: const Color(0xFFE8F5E9),
      surface: Colors.white,
      background: Colors.white,
      error: const Color(0xFFD32F2F),
      onPrimary: Colors.white,
      onPrimaryContainer: Colors.black,
      onSecondary: Colors.white,
      onSecondaryContainer: Colors.black,
      onSurface: Colors.black,
      onBackground: Colors.black,
      onError: Colors.white,
      brightness: Brightness.light,
    ),
    // 优化卡片主题

    // 优化按钮主题
    elevatedButtonTheme: ElevatedButtonThemeData(
      style: ElevatedButton.styleFrom(
        backgroundColor: const Color(0xFF3F51B5),
        foregroundColor: Colors.white,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
        elevation: 2,
      ),
    ),
    textButtonTheme: TextButtonThemeData(
      style: TextButton.styleFrom(foregroundColor: const Color(0xFF3F51B5)),
    ),
    outlinedButtonTheme: OutlinedButtonThemeData(
      style: OutlinedButton.styleFrom(
        foregroundColor: const Color(0xFF3F51B5),
        side: const BorderSide(color: Color(0xFF3F51B5)),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
      ),
    ),
    // 优化输入框主题
    inputDecorationTheme: const InputDecorationTheme(
      fillColor: Colors.white,
      filled: true,
      border: OutlineInputBorder(
        borderSide: BorderSide(color: Color(0xFFE0E0E0)),
        borderRadius: BorderRadius.all(Radius.circular(8)),
      ),
      enabledBorder: OutlineInputBorder(
        borderSide: BorderSide(color: Color(0xFFE0E0E0)),
        borderRadius: BorderRadius.all(Radius.circular(8)),
      ),
      focusedBorder: OutlineInputBorder(
        borderSide: BorderSide(color: Color(0xFF3F51B5)),
        borderRadius: BorderRadius.all(Radius.circular(8)),
      ),
      contentPadding: EdgeInsets.symmetric(horizontal: 16, vertical: 12),
    ),
    // 优化分割线颜色
    dividerTheme: const DividerThemeData(
      color: Color(0xFFE0E0E0),
      thickness: 1,
    ),
  );

  static final dark = ThemeData(
    brightness: Brightness.dark,
    // Deeper primary color for dark theme
    primaryColor: const Color(0xFF3949AB),
    primaryColorLight: const Color(0xFF7986CB),
    primaryColorDark: const Color(0xFF1A237E),
    secondaryHeaderColor: const Color(0xFF4CAF50),
    scaffoldBackgroundColor: const Color(0xFF3949AB),
    // 设置搜索框背景颜色为蓝色
    inputDecorationTheme: const InputDecorationTheme(
      filled: true,
      fillColor: Color(0xFF7986CB), // 使用与primaryColor相同的蓝色
      border: OutlineInputBorder(
        borderRadius: BorderRadius.all(Radius.circular(8)),
        borderSide: BorderSide.none,
      ),
      contentPadding: EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      hintStyle: TextStyle(color: Colors.white70),
      prefixIconColor: Colors.white70,
      suffixIconColor: Colors.white70,
    ),
    // 设置按钮背景颜色为蓝色
    buttonTheme: const ButtonThemeData(
      buttonColor: Color(0xFF3949AB), // 使用与primaryColor相同的蓝色作为按钮背景
      textTheme: ButtonTextTheme.primary,
      padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(8)),
      ),
    ),
    // 为现代按钮组件添加ElevatedButton样式
    elevatedButtonTheme: ElevatedButtonThemeData(
      style: ElevatedButton.styleFrom(
        backgroundColor: const Color(0xFF3949AB), // 设置ElevatedButton背景色为蓝色
        foregroundColor: Colors.white, // 设置按钮文字颜色为白色
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(8),
        ),
      ),
    ),
    // 添加完整的CardTheme配置，确保卡片背景为蓝色
    cardTheme: const CardThemeData(
      // 设置卡片背景颜色为蓝色
      color: Color(0xFF3949AB),
      // 设置文本颜色为黑色以确保与蓝色背景有良好的对比度
      // 添加阴影效果
      shadowColor: Colors.black,
      elevation: 4,
      // 设置卡片圆角
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(12)),
      ),
      // 设置卡片外边距
      margin: EdgeInsets.all(8),
      // 设置表面色调为透明
      surfaceTintColor: Colors.transparent,
    ),
    // Improved text contrast for dark backgrounds
    textTheme: TextTheme(
      bodyMedium: const TextStyle(color: Color(0xFFE0E0E0), fontSize: 16),
      bodySmall: TextStyle(
        color: const Color(0xFFE0E0E0).withOpacity(0.7),
        fontSize: 14,
      ),
      titleLarge: const TextStyle(
        color: Colors.white,
        fontWeight: FontWeight.bold,
        fontSize: 20,
      ),
    ),
    appBarTheme: AppBarTheme(
      color: const Color(0xFF3949AB),
      titleTextStyle: const TextStyle(
        color: Colors.white,
        fontSize: 20,
        fontWeight: FontWeight.w600,
      ),
      elevation: 4,
    ),
    // Added accent color for interactive elements
    colorScheme: ColorScheme.dark(
      primary: const Color(0xFF3949AB),
      secondary: const Color(0xFFFFC107),
      surface: const Color(0xFF1E1E1E),
      error: const Color(0xFFCF6679),
    ),
  );

  static ThemeData system(BuildContext context) {
    final brightness = MediaQuery.of(context).platformBrightness;
    return brightness == Brightness.light ? light : dark;
  }

  // 默认主题
  static final defaultTheme = light; // 使用浅白色主题作为默认
}
