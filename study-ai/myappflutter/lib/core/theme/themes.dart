import 'package:flutter/material.dart';

class Themes {
  static final light = ThemeData(
    brightness: Brightness.light,
    primaryColor: Colors.blue,
    scaffoldBackgroundColor: Colors.white,
    textTheme: TextTheme(
      bodyMedium: TextStyle(color: Colors.black),
      bodySmall: TextStyle(color: Colors.black),
    ),
    appBarTheme: AppBarTheme(
      color: Colors.blue,
      titleTextStyle: TextStyle(color: Colors.white, fontSize: 20),
    ),
  );

  static final dark = ThemeData(
    brightness: Brightness.dark,
    primaryColor: Colors.blueGrey,
    scaffoldBackgroundColor: Colors.grey[900],
    textTheme: TextTheme(
      bodyMedium: TextStyle(color: Colors.white),
      bodySmall: TextStyle(color: Colors.white),
    ),
    appBarTheme: AppBarTheme(
      color: Colors.blueGrey,
      titleTextStyle: TextStyle(color: Colors.white, fontSize: 20),
    ),
  );

  // 默认主题
  static final defaultTheme = light; // 使用浅白色主题作为默认
}
