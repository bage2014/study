
import 'package:flutter/material.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';
import 'package:flutter_study/model/RoutPath.dart';
import 'package:flutter_study/view/home/Home.dart';
import 'package:flutter_study/view/tv/TvPlayer.dart';
import 'package:flutter_study/view/tv/TvList.dart';


class RouteConfiguration {

  static List<RoutPath> paths = [
    RoutPath(RouteNameConstant.route_name_home, (context, match) => Home(),),
    RoutPath(RouteNameConstant.route_name_tv_player, (context, match) => TvPlayer(),),
    RoutPath(RouteNameConstant.route_name_tv_list, (context, match) => TvList(),),
  ];

  static Route<dynamic> onGenerateRoute(RouteSettings settings) {
    print('routeName = ' + settings.name);
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
