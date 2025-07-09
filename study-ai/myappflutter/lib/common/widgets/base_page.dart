import 'package:flutter/material.dart';
import 'package:flutter/services.dart'; // 添加此行导入SystemUiOverlayStyle
import 'package:get/get.dart';
import '../../config/themes.dart';

class BasePage extends StatelessWidget {
  final String title;
  final Widget body;
  final bool showAppBar;
  final List<Widget>? actions;

  const BasePage({
    super.key,
    required this.title,
    required this.body,
    this.showAppBar = true,
    this.actions,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBodyBehindAppBar: showAppBar,
      appBar: showAppBar ? AppBar(
        title: Text(title.tr),
        centerTitle: true,
        actions: actions,
        backgroundColor: Colors.transparent,
        elevation: 0,
        // 已添加正确导入，现在可以正常使用SystemUiOverlayStyle
        systemOverlayStyle: Theme.of(context).brightness == Brightness.dark
            ? SystemUiOverlayStyle.light
            : SystemUiOverlayStyle.dark,
      ) : null,
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [
              Theme.of(context).primaryColorLight,
              Theme.of(context).primaryColorDark,
            ],
          ),
        ),
        child: body,
      ),
    );
  }
}