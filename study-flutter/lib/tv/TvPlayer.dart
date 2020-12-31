import 'package:fijkplayer/fijkplayer.dart';
import 'package:flutter/material.dart';

import 'TvPlayerView.dart';

class TvPlayer extends StatefulWidget {
  final String url="https://cctvakhwh5ca-cntv.akamaized.net/clive/cctv1_2/index.m3u8";

  @override
  _VideoScreenState createState() => _VideoScreenState();
}

class _VideoScreenState extends State<TvPlayer> {
  final FijkPlayer player = FijkPlayer();

  _VideoScreenState();

  @override
  void initState() {
    super.initState();
    player.setDataSource(widget.url, autoPlay: true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text("Fijkplayer Example")),
        body: Container(
          alignment: Alignment.center,
          child: FijkView(
            player: player,
            panelBuilder: (FijkPlayer player, FijkData data, BuildContext context, Size viewSize, Rect texturePos) {
              return CustomFijkPanel(
                  player: player,
                  buildContext: context,
                  viewSize: viewSize,
                  texturePos: texturePos);
            },
          ),
        ));
  }

  @override
  void dispose() {
    super.dispose();
    player.release();
  }
}