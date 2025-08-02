import 'package:flutter/material.dart';

class Themes {
  static final light = ThemeData(
    brightness: Brightness.light,
    // Updated primary color with better visual hierarchy
    primaryColor: const Color(0xFF2196F3),
    primaryColorLight: const Color(0xFF64B5F6),
    primaryColorDark: const Color(0xFF0D47A1),
    secondaryHeaderColor: const Color(0xFF81C784),
    scaffoldBackgroundColor: const Color(0xFFF5F7FA),
    // Enhanced text theme with proper contrast
    textTheme: TextTheme(
      bodyMedium: const TextStyle(color: Color(0xFF2D3243), fontSize: 16),
      bodySmall: TextStyle(
        color: const Color(0xFF2D3243).withOpacity(0.7),
        fontSize: 14,
      ),
      titleLarge: const TextStyle(
        color: Color(0xFF1A1D26),
        fontWeight: FontWeight.bold,
        fontSize: 20,
      ),
    ),
    appBarTheme: AppBarTheme(
      color: const Color(0xFF2196F3),
      titleTextStyle: const TextStyle(
        color: Colors.white,
        fontSize: 20,
        fontWeight: FontWeight.w600,
      ),
      elevation: 4,
    ),
    // Added accent color for interactive elements
    colorScheme: ColorScheme.light(
      primary: const Color(0xFF2196F3),
      secondary: const Color(0xFF00BCD4),
      surface: Colors.white,
      error: const Color(0xFFB00020),
    ),
  );

  static final dark = ThemeData(
    brightness: Brightness.dark,
    // Deeper primary color for dark theme
    primaryColor: const Color(0xFF3949AB),
    primaryColorLight: const Color(0xFF7986CB),
    primaryColorDark: const Color(0xFF1A237E),
    secondaryHeaderColor: const Color(0xFF4CAF50),
    scaffoldBackgroundColor: const Color(0xFF121212),
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

  // 跟随系统主题
  // static final system = ThemeData(
  //   brightness: Brightness.light, // 默认为浅色主题
  //   primaryColor: Colors.blue,
  //   scaffoldBackgroundColor: Colors.white,
  //   textTheme: TextTheme(
  //     bodyMedium: TextStyle(color: Colors.black),
  //     bodySmall: TextStyle(color: Colors.black),
  //   ),
  //   appBarTheme: AppBarTheme(
  //     color: Colors.blue,
  //     titleTextStyle: TextStyle(color: Colors.white, fontSize: 20),
  //   ),
  // );

  static ThemeData system(BuildContext context) {
    final brightness = MediaQuery.of(context).platformBrightness;
    return brightness == Brightness.light ? light : dark;
  }

  // 默认主题
  static final defaultTheme = light; // 使用浅白色主题作为默认
}
