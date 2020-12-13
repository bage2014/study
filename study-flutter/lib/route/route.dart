
import 'package:flutter/material.dart';
import 'package:flutter_study/Home.dart';
import 'package:flutter_study/constant/RouteNameConstant.dart';
import 'package:flutter_study/model/RoutPath.dart';

class RouteConfiguration {

  static List<RoutPath> paths = [
    RoutPath(RouteNameConstant.route_name_home, (context, match) => Home(),
    ),
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
