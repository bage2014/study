import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/view/login/Login.dart';
import 'package:tutorials/view/about/AboutVersions.dart';
import 'package:tutorials/view/login/Register.dart';
import 'package:tutorials/view/settings/Feedbacks.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/constant/RouteNameConstant.dart';
import 'package:tutorials/model/RoutPath.dart';
import 'package:tutorials/view/about/About.dart';
import 'package:tutorials/view/home/Home.dart';
import 'package:tutorials/view/profile/Profile.dart';
import 'package:tutorials/view/about/AboutAuthor.dart';
import 'package:tutorials/view/settings/DevTool.dart';
import 'package:tutorials/view/settings/Settings.dart';

class RouteConfiguration {
  static List<RoutPath> paths = [
    RoutPath(
      RouteNameConstant.route_name_home,
      (context, match) => Home(),
    ),


    RoutPath(
      RouteNameConstant.route_name_login,
          (context, match) => Login(),
    ),
    RoutPath(
      RouteNameConstant.route_name_register,
          (context, match) => Register(),
    ),


    RoutPath(
      RouteNameConstant.route_name_profile,
          (context, match) => Profile(),
    ),


    // todo other page



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
      RouteNameConstant.route_name_about_versions,
          (context, match) => AboutVersions(),
    ),
    RoutPath(
      RouteNameConstant.route_name_setting_dev_tool,
          (context, match) => DevTool(),
    ),
    RoutPath(
      RouteNameConstant.route_name_setting_feedbacks,
          (context, match) => Feedbacks(),
    ),

  ];

  static Route<dynamic>? onGenerateRoute(RouteSettings settings) {
    String? temp = settings?.name;
    String name = temp ?? "";
    Logs.info('routeName = ${name}');
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
