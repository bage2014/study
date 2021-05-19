import 'dart:math';

import 'package:fijkplayer/fijkplayer.dart';
import 'package:flutter/material.dart';

class CustomPanel extends StatefulWidget {
  final FijkPlayer player;
  final BuildContext buildContext;
  final Size viewSize;
  final Rect texturePos;
  final String title;

  const CustomPanel({
    @required this.player,
    this.buildContext,
    this.viewSize,
    this.texturePos,
    this.title,
  });

  @override
  _CustomFijkPanel createState() => _CustomFijkPanel();
}

class _CustomFijkPanel extends State<CustomPanel> {
  FijkPlayer get player => widget.player;

  /// 是否显示状态栏+菜单栏
  bool isPlayShowCont = true;
  bool _playing = true;

  @override
  void initState() {
    /// 初始化
    super.initState();
    widget.player.addListener(_playerValueChanged);
    _playing = true;
  }

  @override
  Widget build(BuildContext context) {
    Rect rect = Rect.fromLTRB(
      max(0.0, widget.texturePos.left),
      max(0.0, widget.texturePos.top),
      min(widget.viewSize.width, widget.texturePos.right),
      min(widget.viewSize.height, widget.texturePos.bottom),
    );

    return Positioned.fromRect(
      rect: rect,
      child: GestureDetector(
        onTap: () {
          setState(() {
            /// 显示 、隐藏  进度条+标题栏
            isPlayShowCont = !isPlayShowCont;
          });
        },
        child: Container(
            color: Color.fromRGBO(0, 0, 0, 0.0),
            alignment: Alignment.bottomLeft,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                // 标题栏
                !isPlayShowCont
                    ? SizedBox()
                    : Container(
                        color: Color.fromRGBO(0, 0, 0, 0.65),
                        height: 35,
                        alignment: Alignment.centerLeft,
                        child: Row(
                          children: <Widget>[
                            IconButton(
                                icon: Icon(
                                  Icons.chevron_left,
                                  color: Colors.white,
                                ),
                                onPressed: () {
                                  Navigator.pop(context);
                                }),
                            Text(
                              widget.title,
                              style: TextStyle(color: Colors.white,fontSize: 16.0,),
                            )
                          ],
                        ),
                      ),

                // loading
                _playing || widget.player.state == FijkState.paused
                    ? SizedBox()
                    : Container(
                        height: 35,
                        alignment: Alignment.center,
                        child: CircularProgressIndicator(),
                      ),

                /// 控制条
                !isPlayShowCont
                    ? SizedBox()
                    : Container(
                        color: Color.fromRGBO(0, 0, 0, 0.65),
                        height: 50,
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: <Widget>[
                            IconButton(
                              icon: Icon(
                                _playing ? Icons.pause : Icons.play_arrow,
                                color: Colors.white,
                              ),
                              onPressed: () => toggle(
                                  widget.player.state == FijkState.started),
                            ),
                            IconButton(
                              alignment: Alignment.bottomRight,
                              icon: Icon(
                                widget.player.value.fullScreen
                                    ? Icons.fullscreen_exit
                                    : Icons.fullscreen,
                                color: Colors.white,
                              ),
                              onPressed: () => widget.player.value.fullScreen
                                  ? widget.player.exitFullScreen()
                                  : widget.player.enterFullScreen(),
                            )
                          ],
                        ),
                      )
              ],
            )),
      ),
    );
  }

  @override
  void dispose() {
    player.removeListener(_playerValueChanged);
    /// 关闭流回调
    super.dispose();
  }

  void _playerValueChanged() {
    bool playing = player.value.state == FijkState.started;
    if (playing != _playing) {
      setState(() {
        _playing = playing;
      });
    }
  }

  void toggle(bool started) {
    setState(() {
      /// 显示 、隐藏  进度条+标题栏
      _playing = started;
    });
    if (started) {
      widget.player.pause();
      return;
    }
    widget.player.start();
  }
}
