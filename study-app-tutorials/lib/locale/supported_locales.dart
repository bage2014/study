import 'package:flutter/material.dart';

class SupportedLocales {
  static Locale zhLocale = const Locale('zh', 'CN'); // 中文简体
  static Locale enLocale = const Locale('en', 'US'); // 美国英语

  static Locale defaultLocale = zhLocale; // 中文简体

  static List<Locale> locales = [
    zhLocale,
    enLocale,
  ];
}
