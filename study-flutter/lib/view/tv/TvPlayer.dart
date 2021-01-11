import 'package:fijkplayer/fijkplayer.dart';
import 'package:flutter/material.dart';
import 'package:flutter_study/model/QueryTvResult.dart';
import 'CustomPanel.dart';

class TvPlayer extends StatefulWidget {

  @override
  _VideoScreenState createState() => _VideoScreenState();

}

class _VideoScreenState extends State<TvPlayer> {
  final FijkPlayer player = FijkPlayer();
  bool _loading = true;

  _VideoScreenState();

  @override
  void initState() {
    super.initState();
    player.setOption(FijkOption.hostCategory, "request-screen-on", 1);
    player.setOption(FijkOption.hostCategory, "request-audio-focus", 1);
//    player.enterFullScreen();
  }

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    TvItem arguments = ModalRoute
        .of(context)
        .settings
        .arguments;

    if (player.dataSource == null) {
      player.setDataSource(arguments.url, autoPlay: true,showCover: true);
//      player.set
    }

    return Scaffold(
        body: new Offstage(
          offstage: _loading, //这里控制
          child: Container(
            alignment: Alignment.center,
            child: FijkView(
              player: player,
              panelBuilder: (FijkPlayer player, FijkData data,
                  BuildContext context,
                  Size viewSize, Rect texturePos) {
                return CustomPanel(
                    player: player,
                    buildContext: context,
                    viewSize: viewSize,
                    texturePos: texturePos);
              },
            ),
          ),
        ),
        );
  }

  @override
  void dispose() {
    super.dispose();
    player.release();
  }
}
