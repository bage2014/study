import 'package:flutter/material.dart';

class AppBarOnlyBack extends AppBar {
  final Color color = Colors.black;

  AppBarOnlyBack();

  @override
  AppBar build(BuildContext context) {
    return AppBar(
      backgroundColor: Colors.transparent,
      elevation: 0,
      iconTheme: IconThemeData(color: color),
    );
  }
}
