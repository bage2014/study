import 'package:flutter/material.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:video_player/video_player.dart';
import 'dart:async'; // 添加这行导入语句
import '../widgets/base_page.dart';
import '../../data/models/tv_model.dart'; // 导入 TvChannel 模型

class TvPlayerPage extends StatefulWidget {
  final TvChannel? channel; // 接收 TV Channel 对象，设为可选
  final String? streamUrl; // 保留原有的 streamUrl 参数，也设为可选

  const TvPlayerPage({super.key, this.channel, this.streamUrl});

  @override
  State<TvPlayerPage> createState() => _TvPlayerPageState();
}

class _TvPlayerPageState extends State<TvPlayerPage> {
  late VideoPlayerController _controller;
  bool _isInitialized = false;
  bool _isLoading = true;
  String? _effectiveStreamUrl;
  String _channelTitle = 'TV Player'; // 默认标题

  @override
  void initState() {
    super.initState();
    // 确定要使用的流URL
    _determineStreamUrl();
    _initializePlayer();
  }

  void _determineStreamUrl() {
    // 优先使用传入的 channel 对象中的第一个 URL
    if (widget.channel != null &&
        widget.channel!.channelUrls != null &&
        widget.channel!.channelUrls!.isNotEmpty) {
      _effectiveStreamUrl = widget.channel!.channelUrls![0].url;
      _channelTitle = widget.channel!.title ?? 'TV Player';
      LogUtil.info('Channel Title: $_channelTitle');
      LogUtil.info('Effective Stream URL: $_effectiveStreamUrl');
    } else if (widget.streamUrl != null) {
      // 其次使用直接传入的 streamUrl
      _effectiveStreamUrl = widget.streamUrl;
      _channelTitle = 'TV Player';
      LogUtil.info('Effective Stream URL: $_effectiveStreamUrl');
    } else {
      // 如果都没有传入，则使用一个默认的示例 URL 或显示错误
      _effectiveStreamUrl =
          'https://cctvakhwh5ca-cntv.akamaized.net/clive/cctv1_2/index.m3u8';
      LogUtil.info('default Effective Stream URL: $_effectiveStreamUrl');
    }
  }

  Future<void> _initializePlayer() async {
    try {
      // 初始化视频控制器
      _controller = VideoPlayerController.networkUrl(
        Uri.parse(_effectiveStreamUrl ?? ''),
        httpHeaders: {
          'User-Agent':
              'Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Mobile Safari/537.36',
        },
      );

      // 监听初始化状态变化
      _controller.addListener(() {
        if (_controller.value.isInitialized && !_isInitialized) {
          setState(() {
            _isInitialized = true;
            _isLoading = false;
          });
        }

        // 监听播放状态，自动重播直播流
        if (_controller.value.isCompleted) {
          _controller.seekTo(Duration.zero);
          _controller.play();
        }
      });

      // 初始化并自动播放
      await _controller.initialize();
      await _controller.play();
    } catch (error) {
      setState(() {
        _isLoading = false;
      });
      if (context.mounted) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(SnackBar(content: Text('无法加载视频: $error')));
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: _channelTitle, // 使用频道标题
      body: Column(
        children: [
          Expanded(
            child: _isLoading
                ? const Center(child: CircularProgressIndicator())
                : _isInitialized
                ? AspectRatio(
                    aspectRatio: _controller.value.aspectRatio,
                    child: Stack(
                      children: [
                        VideoPlayer(_controller),
                        VideoControlsOverlay(controller: _controller),
                      ],
                    ),
                  )
                : const Center(child: Text('无法初始化播放器')),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }
}

// 自定义视频控制覆盖层
class VideoControlsOverlay extends StatefulWidget {
  final VideoPlayerController controller;

  const VideoControlsOverlay({super.key, required this.controller});

  @override
  State<VideoControlsOverlay> createState() => _VideoControlsOverlayState();
}

class _VideoControlsOverlayState extends State<VideoControlsOverlay> {
  bool _showControls = true;
  Timer? _hideTimer;

  @override
  void initState() {
    super.initState();
    widget.controller.addListener(() {
      if (!widget.controller.value.isPlaying && _showControls) {
        _startHideTimer();
      }
    });
  }

  void _startHideTimer() {
    _hideTimer?.cancel();
    _hideTimer = Timer(const Duration(seconds: 3), () {
      if (mounted && widget.controller.value.isPlaying) {
        setState(() {
          _showControls = false;
        });
      }
    });
  }

  void _toggleControls() {
    setState(() {
      _showControls = !_showControls;
      if (_showControls) {
        _startHideTimer();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: _toggleControls,
      child: Stack(
        children: [
          AnimatedOpacity(
            opacity: _showControls ? 1.0 : 0.0,
            duration: const Duration(milliseconds: 300),
            child: ColoredBox(
              color: Colors.black26,
              child: Center(
                child: IconButton(
                  icon: Icon(
                    widget.controller.value.isPlaying
                        ? Icons.pause
                        : Icons.play_arrow,
                    size: 64,
                    color: Colors.white,
                  ),
                  onPressed: () {
                    setState(() {
                      widget.controller.value.isPlaying
                          ? widget.controller.pause()
                          : widget.controller.play();
                    });
                  },
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    _hideTimer?.cancel();
    super.dispose();
  }
}
