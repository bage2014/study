import 'package:flutter/material.dart';

class MainViewWrapper extends StatefulWidget {
  late Widget _child;
  late bool _isLoading;

  MainViewWrapper(Widget child,bool isLoading, {Key? key}) : super(key: key) {
    _child = child;
    _isLoading = isLoading;
  }

  @override
  State<MainViewWrapper> createState() =>
      _MainViewWrapperState(_child, _isLoading);

}

class _MainViewWrapperState extends State<MainViewWrapper> {
  Widget _child = const SizedBox();
  bool _isLoading = false;

  _MainViewWrapperState(Widget child, bool isLoading) {
    _child = child;
    _isLoading = isLoading;
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      alignment: Alignment.center, //指定未定位或部分定位widget的对齐方式
      children: <Widget>[
        Scaffold(
          backgroundColor: const Color(0xFFFCFCFC),
          body: SafeArea(
            child: _child,
          ),
        ),
        Container(
          child: _isLoading
              ? Container(
                  color: Colors.black54.withOpacity(0.5),
                  width: double.infinity,
                )
              : null,
        ),
        Container(
          child: _isLoading
              ? CircularProgressIndicator(
                  backgroundColor: Colors.grey[200],
                  valueColor: const AlwaysStoppedAnimation(Colors.blue),
                )
              : null,
        ),
      ],
    );
  }
}
