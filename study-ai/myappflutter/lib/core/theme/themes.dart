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
