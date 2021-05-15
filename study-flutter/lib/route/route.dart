import 'package:flutter/material.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';
import 'package:flutter_study/model/RoutPath.dart';
import 'package:flutter_study/view/about/About.dart';
import 'package:flutter_study/view/home/Home.dart';
import 'package:flutter_study/view/profile/Profile.dart';
import 'package:flutter_study/view/settings/DevelopSetting.dart';
import 'package:flutter_study/view/settings/Settings.dart';
import 'package:flutter_study/view/tv/TvList.dart';
import 'package:flutter_study/view/tv/TvPlayer.dart';

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
      RouteNameConstant.route_name_tv_list,
      (context, match) => TvList(),
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
      RouteNameConstant.route_name_setting_develop,
          (context, match) => DevelopSetting(),
    ),
    RoutPath(
      RouteNameConstant.route_name_profile,
          (context, match) => Profile(),
    ),
  ];

  static Route<dynamic> onGenerateRoute(RouteSettings settings) {
    print('routeName = ' + settings.name);
    // 有限匹配相等的
    for (final path in paths) {
      if(settings.name.compareTo(path.pattern) == 0){
        return MaterialPageRoute<void>(
          builder: (context) => path.builder(context, path.pattern),
          settings: settings,
        );
      }
    }
    // 在匹配正则的
    for (final path in paths) {
      final regExpPattern = RegExp(path.pattern);
      if (regExpPattern.hasMatch(settings.name)) {
        final firstMatch = regExpPattern.firstMatch(settings.name);
        final match = (firstMatch.groupCount == 1) ? firstMatch.group(1) : null;
        return MaterialPageRoute<void>(
          builder: (context) => path.builder(context, match),
          settings: settings,
        );
      }
    }

    // If no match was found, we let [WidgetsApp.onUnknownRoute] handle it.
    return null;
  }
}
