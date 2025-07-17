import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:path_provider/path_provider.dart';
import 'dart:io';
import '../../data/api/http_client.dart';

class UpdatePage extends StatefulWidget {
  final String version;

  const UpdatePage({super.key, required this.version});

  @override
  State<UpdatePage> createState() => _UpdatePageState();
}

class _UpdatePageState extends State<UpdatePage> {
  double _progress = 0;
  bool _isDownloading = false;

  Future<void> _downloadUpdate() async {
    setState(() {
      _isDownloading = true;
      _progress = 0;
    });

    try {
      final httpClient = HttpClient();
      final response = await httpClient.get('/download/${widget.version}');

      final dir = await getTemporaryDirectory();
      final file = File('${dir.path}/app_update.apk');

      await file.writeAsBytes(response['data']);

      // 安装APK (Android only)
      if (Platform.isAndroid) {
        // 使用插件如 'open_file' 或 'install_plugin' 来安装APK
      }

      Get.snackbar('成功', '更新已下载并安装');
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
            if (_isDownloading) CircularProgressIndicator(value: _progress),
            ElevatedButton(
              onPressed: _isDownloading ? null : _downloadUpdate,
              child: Text(_isDownloading ? '下载中...' : '开始下载'),
            ),
          ],
        ),
      ),
    );
  }
}
