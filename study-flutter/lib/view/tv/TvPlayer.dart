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

  _VideoScreenState();

  @override
  void initState() {
    super.initState();
    player.setOption(FijkOption.hostCategory, "request-screen-on", 1);
    player.setOption(FijkOption.hostCategory, "request-audio-focus", 1);
  }

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    TvItem arguments = ModalRoute.of(context).settings.arguments;

    if (player.dataSource == null) {
      player.setDataSource(arguments.url, autoPlay: true);
    }

    return Scaffold(
        body: Center(
            child: FijkView(
      player: player,
      panelBuilder: (FijkPlayer player, FijkData data, BuildContext context,
          Size viewSize, Rect texturePos) {
        return CustomPanel(
            player: player,
            buildContext: context,
            viewSize: viewSize,
            texturePos: texturePos,
            title: arguments.name);
      },
    )));
  }

  @override
  void dispose() {
    super.dispose();
    player.release();
  }
}
