import 'package:flutter/material.dart';
// import 'package:better_player/better_player.dart';
import '../../core/constants/constants.dart';
import '../widgets/base_page.dart';

class TvPlayerPage extends StatefulWidget {
  final String streamUrl;

  const TvPlayerPage({super.key, required this.streamUrl});

  @override
  State<TvPlayerPage> createState() => _TvPlayerPageState();
}

class _TvPlayerPageState extends State<TvPlayerPage> {
  // late BetterPlayerController _betterPlayerController;

  @override
  void initState() {
    super.initState();
    _initPlayer();
  }

  void _initPlayer() {
    // final betterPlayerConfiguration = BetterPlayerConfiguration(
    //   aspectRatio: 16 / 9,
    //   fit: BoxFit.contain,
    //   autoPlay: true,
    //   looping: true,
    //   showControlsOnInitialize: true,
    // );

    // final dataSource = BetterPlayerDataSource(
    //   BetterPlayerDataSourceType.network,
    //   widget.streamUrl,
    //   headers: {
    //     'User-Agent': 'Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Mobile Safari/537.36',
    //   },
    // );

    // _betterPlayerController = BetterPlayerController(betterPlayerConfiguration);
    // _betterPlayerController.setupDataSource(dataSource);
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'TV Player',
      body: Container(),
      // body: Column(
      //   children: [
      //     Expanded(
      //       child: BetterPlayerWidget(
      //         controller: _betterPlayerController,
      //       ),
      //     ),
      //   ],
      // ),
    );
  }

  @override
  void dispose() {
    // _betterPlayerController.dispose();
    super.dispose();
  }
}
