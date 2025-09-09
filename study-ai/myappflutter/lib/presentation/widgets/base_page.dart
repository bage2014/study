import 'package:flutter/material.dart';
import 'package:flutter/services.dart'; // 添加此行导入SystemUiOverlayStyle
import 'package:get/get.dart';

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
      // 移除extendBodyBehindAppBar属性，这样内容就不会延伸到AppBar后面
      // extendBodyBehindAppBar: showAppBar,
      appBar: showAppBar
          ? AppBar(
              title: Text(title.tr),
              centerTitle: true,
              actions: actions,
              // 移除透明背景设置，使用主题默认背景色
              // backgroundColor: Colors.transparent,
              elevation: 0,
              // 已添加正确导入，现在可以正常使用SystemUiOverlayStyle
              systemOverlayStyle:
                  Theme.of(context).brightness == Brightness.dark
                  ? SystemUiOverlayStyle.light
                  : SystemUiOverlayStyle.dark,
            )
          : null,
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
