import 'package:flutter/material.dart';

class Themes {
  // 浅蓝色主题
  static final lightBlue = ThemeData(
    primarySwatch: Colors.blue,
    primaryColor: Colors.blue,
    primaryColorLight: Color(0xFFE3F2FD),
    primaryColorDark: Color(0xFF1976D2),
    visualDensity: VisualDensity.adaptivePlatformDensity,
    brightness: Brightness.light,
    appBarTheme: AppBarTheme(
      titleTextStyle: TextStyle(color: Colors.black87, fontSize: 20),
      iconTheme: IconThemeData(color: Colors.black87),
    ),
  );

  // 浅绿色主题
  static final lightGreen = ThemeData(
    primarySwatch: Colors.green,
    primaryColor: Colors.green,
    primaryColorLight: Color(0xFFE8F5E9),
    primaryColorDark: Color(0xFF2E7D32),
    visualDensity: VisualDensity.adaptivePlatformDensity,
    brightness: Brightness.light,
    appBarTheme: AppBarTheme(
      titleTextStyle: TextStyle(color: Colors.black87, fontSize: 20),
      iconTheme: IconThemeData(color: Colors.black87),
    ),
  );

  // 暗色主题
  static final dark = ThemeData(
    primarySwatch: Colors.grey,
    primaryColor: Colors.grey[800],
    primaryColorLight: Color(0xFF424242),
    primaryColorDark: Color(0xFF212121),
    visualDensity: VisualDensity.adaptivePlatformDensity,
    brightness: Brightness.dark,
    appBarTheme: AppBarTheme(
      titleTextStyle: TextStyle(color: Colors.white, fontSize: 20),
      iconTheme: IconThemeData(color: Colors.white),
    ),
  );
  // 浅白色主题
  static final light = ThemeData(
    primarySwatch: Colors.blueGrey,
    appBarTheme: AppBarTheme(
      backgroundColor: Colors.white,
      elevation: 1,
      titleTextStyle: TextStyle(
        color: Colors.black87,
        fontSize: 20,
        fontWeight: FontWeight.bold,
      ),
      iconTheme: IconThemeData(color: Colors.black87),
    ),
    scaffoldBackgroundColor: Colors.white,
  );

  // 默认主题
  static final defaultTheme = light; // 使用浅白色主题作为默认
}
