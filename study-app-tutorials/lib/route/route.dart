import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/view/home/statistics.dart';
import 'package:tutorials/view/login/forget_password_finish.dart';
import 'package:tutorials/view/login/login.dart';
import 'package:tutorials/view/about/about_versions.dart';
import 'package:tutorials/view/login/forget_password_verify.dart';
import 'package:tutorials/view/login/register.dart';
import 'package:tutorials/view/login/forget_password.dart';
import 'package:tutorials/view/login/register_finish.dart';
import 'package:tutorials/view/login/register_verify.dart';
import 'package:tutorials/view/name.card/name_card_edit.dart';
import 'package:tutorials/view/name.card/name_cart.dart';
import 'package:tutorials/view/school.card/school_card.dart';
import 'package:tutorials/view/school.card/school_card_edit.dart';
import 'package:tutorials/view/settings/env.dart';
import 'package:tutorials/view/settings/env_edit.dart';
import 'package:tutorials/view/settings/feedbacks.dart';
import 'package:flutter/material.dart';
import 'package:tutorials/constant/route_constant.dart';
import 'package:tutorials/model/route_path.dart';
import 'package:tutorials/view/about/about.dart';
import 'package:tutorials/view/home/home.dart';
import 'package:tutorials/view/profile/profile_activity.dart';
import 'package:tutorials/view/about/about_author.dart';
import 'package:tutorials/view/settings/dev_tool.dart';
import 'package:tutorials/view/settings/settings.dart';
import 'package:tutorials/view/tv/tv_list.dart';
import 'package:tutorials/view/tv/tv_player.dart';

class RouteConfiguration {
  static List<RoutePath> paths = [
    RoutePath(
      RouteNameConstant.route_name_home,
      (context, match) => Home(),
    ),
    RoutePath(
      RouteNameConstant.route_name_home_statistics,
          (context, match) => Statistics(),
    ),


    RoutePath(
      RouteNameConstant.route_name_login,
          (context, match) => Login(),
    ),
    RoutePath(
      RouteNameConstant.route_name_register,
          (context, match) => Register(),
    ),
    RoutePath(
      RouteNameConstant.route_name_register_verify,
          (context, match) => RegisterVerify(),
    ),
    RoutePath(
      RouteNameConstant.route_name_register_finish,
          (context, match) => RegisterFinish(),
    ),

    RoutePath(
      RouteNameConstant.route_name_forget_password,
          (context, match) => ForgetPassword(),
    ),
    RoutePath(
      RouteNameConstant.route_name_forget_password_verify,
          (context, match) => ForgetPasswordVerify(),
    ),
    RoutePath(
      RouteNameConstant.route_name_forget_password_finish,
          (context, match) => ForgetPasswordFinish(),
    ),

    RoutePath(
      RouteNameConstant.route_name_profile,
          (context, match) => ProfileActivity(),
    ),

    RoutePath(
      RouteNameConstant.route_name_settings,
          (context, match) => Settings(),
    ),
    RoutePath(
      RouteNameConstant.route_name_about,
          (context, match) => About(),
    ),
    RoutePath(
      RouteNameConstant.route_name_about_author,
          (context, match) => AboutAuthor(),
    ),
    RoutePath(
      RouteNameConstant.route_name_about_versions,
          (context, match) => AboutVersions(),
    ),
    RoutePath(
      RouteNameConstant.route_name_setting_dev_tool,
          (context, match) => DevTool(),
    ),
    RoutePath(
      RouteNameConstant.route_name_setting_feedbacks,
          (context, match) => Feedbacks(),
    ),

    RoutePath(
      RouteNameConstant.route_name_school_card,
          (context, match) => SchoolCard(),
    ),
    RoutePath(
      RouteNameConstant.route_name_school_card_edit,
          (context, match) => const SchoolCardEdit(),
    ),

    RoutePath(
      RouteNameConstant.route_name_name_card,
          (context, match) => NameCard(),
    ),
    RoutePath(
      RouteNameConstant.route_name_name_card_edit,
          (context, match) => NameCardEdit(),
    ),
    RoutePath(
      RouteNameConstant.route_name_env,
          (context, match) => Environment(),
    ),
    RoutePath(
      RouteNameConstant.route_name_env_edit,
          (context, match) => EnvironmentEdit(),
    ),

    RoutePath(
      RouteNameConstant.route_name_tv_list,
          (context, match) => const TVList(),
    ),
    RoutePath(
      RouteNameConstant.route_name_tv_player,
          (context, match) => TvPlayer(),
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
