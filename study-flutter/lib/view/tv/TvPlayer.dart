import 'package:fijkplayer/fijkplayer.dart';
import 'package:flutter/material.dart';
import 'CustomPanel.dart';

class TvPlayer extends StatefulWidget {
  final String url =
      "https://cctvakhwh5ca-cntv.akamaized.net/clive/cctv1_2/index.m3u8";
  //       "http://117.169.120.140:8080/live/cctv-10/.m3u8";

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
    player.setDataSource(widget.url, autoPlay: true);
//    player.enterFullScreen();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Container(
          alignment: Alignment.center,
          child: FijkView(
            player: player,
            panelBuilder: (FijkPlayer player, FijkData data, BuildContext context,
                Size viewSize, Rect texturePos) {
              return CustomPanel(
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
