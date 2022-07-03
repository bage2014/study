import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/view/home/statistics.dart';
import 'package:tutorials/view/login/forget_password_finish.dart';
import 'package:tutorials/view/login/login.dart';
import 'package:tutorials/view/about/AboutVersions.dart';
import 'package:tutorials/view/login/forget_password_verify.dart';
import 'package:tutorials/view/login/register.dart';
import 'package:tutorials/view/login/forget_password.dart';
import 'package:tutorials/view/login/register_finish.dart';
import 'package:tutorials/view/login/register_verify.dart';
import 'package:tutorials/view/name.card/name_card.dart';
import 'package:tutorials/view/settings/feedbacks.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/model/RoutPath.dart';
import 'package:tutorials/view/about/About.dart';
import 'package:tutorials/view/home/home.dart';
import 'package:tutorials/view/profile/profile.dart';
import 'package:tutorials/view/about/AboutAuthor.dart';
import 'package:tutorials/view/settings/dev_tool.dart';
import 'package:tutorials/view/settings/settings.dart';

class RouteConfiguration {
  static List<RoutPath> paths = [
    RoutPath(
      RouteNameConstant.route_name_home,
      (context, match) => Home(),
    ),
    RoutPath(
      RouteNameConstant.route_name_home_statistics,
          (context, match) => Statistics(),
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
      RouteNameConstant.route_name_register_verify,
          (context, match) => RegisterVerify(),
    ),
    RoutPath(
      RouteNameConstant.route_name_register_finish,
          (context, match) => RegisterFinish(),
    ),

    RoutPath(
      RouteNameConstant.route_name_forget_password,
          (context, match) => ForgetPassword(),
    ),
    RoutPath(
      RouteNameConstant.route_name_forget_password_verify,
          (context, match) => ForgetPasswordVerify(),
    ),
    RoutPath(
      RouteNameConstant.route_name_forget_password_finish,
          (context, match) => ForgetPasswordFinish(),
    ),

    RoutPath(
      RouteNameConstant.route_name_profile,
          (context, match) => Profile(),
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


    RoutPath(
      RouteNameConstant.route_name_name_card,
          (context, match) => NameCard(),
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
