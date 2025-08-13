import 'dart:convert';

// import 'package:fijkplayer/fijkplayer.dart';
import 'package:flutter/material.dart';

import 'CustomPanel.dart';

class TvPlayer extends StatefulWidget {
  @override
  _VideoScreenState createState() => _VideoScreenState();
}

class _VideoScreenState extends State<TvPlayer> {
  // final FijkPlayer player = FijkPlayer();

  _VideoScreenState();

  @override
  void initState() {
    super.initState();
    // player.setOption(FijkOption.hostCategory, "request-screen-on", 1);
    // player.setOption(FijkOption.hostCategory, "request-audio-focus", 1);
  }

  @override
  Widget build(BuildContext context) {
    //获取路由参数
    // TvItem arguments = ModalRoute.of(context)?.settings.arguments as TvItem;

    // if (player.dataSource == null) {
    //   player.setDataSource(
    //     'http://117.169.120.140:8080/live/cctv-1/.m3u8' ?? "",
    //     autoPlay: true,
    //   );
    // }

    return Scaffold(
      body: Center(
        child: Container(width: 100, height: 100, color: Colors.red),
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();
    // player.release();
  }
}
