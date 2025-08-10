import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';  // 添加这一行

class UpdatePage extends StatefulWidget {
  final String version;

  const UpdatePage({super.key, required this.version});

  @override
  State<UpdatePage> createState() => _UpdatePageState();
}

class _UpdatePageState extends State<UpdatePage> {
  bool _isDownloading = false;

  Future<void> _downloadUpdate() async {
    setState(() {
      _isDownloading = true;
    });

    try {
      // 构建下载URL
      final downloadUrl = 'https://your-api-domain.com/download/${widget.version}';

      // 打开浏览器下载
      final uri = Uri.parse(downloadUrl);
      if (await canLaunchUrl(uri)) {
        await launchUrl(uri, mode: LaunchMode.externalApplication);
        Get.snackbar('成功', '浏览器已打开，开始下载更新');
      } else {
        throw Exception('无法打开浏览器');
      }
    } catch (e) {
      Get.snackbar('错误', '下载失败: ${e.toString()}');
    } finally {
      setState(() {
        _isDownloading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('更新到版本 ${widget.version}')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (_isDownloading) const CircularProgressIndicator(),
            ElevatedButton(
              onPressed: _isDownloading ? null : _downloadUpdate,
              child: Text(_isDownloading ? '处理中...' : '开始下载'),
            ),
          ],
        ),
      ),
    );
  }
}
