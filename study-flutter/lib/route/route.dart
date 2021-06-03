import 'package:flutter/material.dart';
import 'package:app_lu_lu/constant/RouteNameConstant.dart';
import 'package:app_lu_lu/model/RoutPath.dart';
import 'package:app_lu_lu/view/about/About.dart';
import 'package:app_lu_lu/view/home/Home.dart';
import 'package:app_lu_lu/view/profile/Profile.dart';
import 'package:app_lu_lu/view/about/AboutAuthor.dart';
import 'package:app_lu_lu/view/settings/DevTool.dart';
import 'package:app_lu_lu/view/settings/Settings.dart';
import 'package:app_lu_lu/view/tv/TV.dart';
import 'package:app_lu_lu/view/tv/TvPlayer.dart';

class RouteConfiguration {
  static List<RoutPath> paths = [
    RoutPath(
      RouteNameConstant.route_name_home,
      (context, match) => Home(),
    ),
    RoutPath(
      RouteNameConstant.route_name_tv_player,
      (context, match) => TvPlayer(),
    ),
    RoutPath(
      RouteNameConstant.route_name_tv,
      (context, match) => TV(),
    ),
    RoutPath(
      RouteNameConstant.route_name_settings,
      (context, match) => Settings(),
    ),
    RoutPath(
      RouteNameConstant.route_name_about,
      (context, match) => About(),
    ),
    RoutPath(
      RouteNameConstant.route_name_about_author,
      (context, match) => AboutAuthor(),
    ),
    RoutPath(
      RouteNameConstant.route_name_setting_dev_tool,
      (context, match) => DevTool(),
    ),
    RoutPath(
      RouteNameConstant.route_name_profile,
      (context, match) => Profile(),
    ),
  ];

  static Route<dynamic>? onGenerateRoute(RouteSettings settings) {
    String? temp = settings?.name;
    String name = temp == null ? "" : temp;
    print('routeName = {name}');
    // 有限匹配相等的
    for (final path in paths) {
      if (name.compareTo(path.pattern) == 0) {
        return MaterialPageRoute<void>(
          builder: (context) => path.builder(context, path.pattern),
          settings: settings,
        );
      }
    }
    // 在匹配正则的
    for (final path in paths) {
      final regExpPattern = RegExp(path.pattern);
      if (regExpPattern.hasMatch(name)) {
        final firstMatch = regExpPattern.firstMatch(name);
        final match = (firstMatch?.groupCount == 1) ? firstMatch?.group(1) : null;
        return MaterialPageRoute<void>(
          builder: (context) => path.builder(context, match == null ? "" : match),
          settings: settings,
        );
      }
    }
    // If no match was found, we let [WidgetsApp.onUnknownRoute] handle it.
    return null;
  }
}
