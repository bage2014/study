import 'package:flutter/material.dart';
import 'package:tutorials/constant/color_constant.dart';

class AppBarOnlyBack extends AppBar {

  AppBarOnlyBack();

  @override
  Widget build(BuildContext context) {
    return AppBar(
      backgroundColor: Colors.transparent,
      elevation: 0,
      iconTheme: IconThemeData(color: ColorConstant.app_bar_only_back_color),
    );
  }
}
