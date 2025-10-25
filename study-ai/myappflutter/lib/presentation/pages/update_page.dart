import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:myappflutter/data/api/http_client.dart'; // 导入HttpClient组件
import 'package:myappflutter/data/models/version_model.dart';
import 'package:path_provider/path_provider.dart'; // 导入路径提供者
import 'package:path/path.dart' as path; // 导入路径处理
import 'package:open_file/open_file.dart'; // 导入文件打开功能
import 'package:permission_handler/permission_handler.dart'; // 导入权限处理
import 'dart:io'; // 导入IO操作

class UpdatePage extends StatefulWidget {
  final Version version;

  const UpdatePage({super.key, required this.version});

  @override
  State<UpdatePage> createState() => _UpdatePageState();
}

class _UpdatePageState extends State<UpdatePage> {
  bool _isDownloading = false;
  bool _isDownloadingApp = false;
  double _downloadProgress = 0.0; // 下载进度 (0.0 - 1.0)
  final HttpClient _httpClient = HttpClient(); // 创建HttpClient实例

  // 检查安装权限
  Future<bool> _checkInstallPermission() async {
    // REQUEST_INSTALL_PACKAGES 是特殊权限，需要通过系统设置页面授权
    if (await Permission.requestInstallPackages.isGranted) {
      return true;
    }
    return false;
  }

  // 请求安装权限
  Future<bool> _requestInstallPermission() async {
    try {
      // 跳转到系统设置页面让用户手动授权
      final bool result = await openAppSettings();
      if (result) {
        // 用户已跳转到设置页面，等待用户返回
        Get.snackbar('prompt'.tr, '请在系统设置中授权"安装未知应用"权限');
        return true;
      }
      return false;
    } catch (e) {
      LogUtil.error('请求安装权限失败: $e');
      return false;
    }
  }

  // 检查并请求安装权限
  Future<bool> _checkAndRequestInstallPermission() async {
    // 先检查是否已有权限
    if (await _checkInstallPermission()) {
      return true;
    }

    // 如果没有权限，请求用户授权
    Get.snackbar('prompt'.tr, '需要授权安装未知应用权限才能安装APK');

    // 显示确认对话框
    final bool confirm =
        await Get.dialog<bool>(
          AlertDialog(
            title: Text('install_permission'.tr),
            content: Text('install_permission_required'.tr),
            actions: [
              TextButton(
                onPressed: () => Get.back(result: false),
                child: Text('cancel'.tr),
              ),
              TextButton(
                onPressed: () => Get.back(result: true),
                child: Text('go_to_authorize'.tr),
              ),
            ],
          ),
        ) ??
        false;

    if (!confirm) {
      return false;
    }

    // 跳转到系统设置页面
    return await _requestInstallPermission();
  }

  Future<void> _downloadUpdate() async {
    setState(() {
      _isDownloading = true;
    });

    try {
      // 使用HttpClient构建下载URL
      final downloadUrl = _httpClient
          .buildUri(
            '/app/download/${widget.version.fileId ?? widget.version.id ?? widget.version.version}',
            null,
          )
          .toString();
      LogUtil.info('下载URL: $downloadUrl');

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

  // 方法2：应用内下载APK文件并安装
  Future<void> _downloadAndInstallApp() async {
    setState(() {
      _isDownloadingApp = true;
    });

    try {
      // 检查并请求安装权限
      final bool hasPermission = await _checkAndRequestInstallPermission();
      if (!hasPermission) {
        Get.snackbar('prompt'.tr, '安装权限未授权，无法安装APK');
        return;
      }

      // 构建APK下载URL
      final downloadUrl = _httpClient
          .buildUri(
            '/app/download/${widget.version.fileId ?? widget.version.id ?? widget.version.version}',
            null,
          )
          .toString();

      LogUtil.info('APK下载URL: $downloadUrl');

      // 验证URL格式
      if (downloadUrl.isEmpty || !downloadUrl.startsWith('http')) {
        throw Exception('APK下载URL格式不正确: $downloadUrl');
      }

      // 获取应用存储目录
      final Directory appDocDir = await getApplicationDocumentsDirectory();
      final String appDocPath = appDocDir.path;

      // 创建下载目录
      final String downloadDir = path.join(appDocPath, 'downloads');
      final Directory dir = Directory(downloadDir);
      if (!await dir.exists()) {
        await dir.create(recursive: true);
      }

      // 构建APK文件名
      final String fileName = 'myapp_v${widget.version.version}.apk';
      final String filePath = path.join(downloadDir, fileName);

      // 下载APK文件
      Get.snackbar('提示', '开始下载APK文件...');

      await _httpClient.download(
        downloadUrl,
        filePath,
        onReceiveProgress: (received, total) {
          if (total != -1) {
            final progress = (received / total * 100).toStringAsFixed(0);
            LogUtil.info('下载进度: $progress%');

            // 更新下载进度（可选：可以显示进度条）
            setState(() {
              // 这里可以添加进度状态
              _downloadProgress = received / total * 100;
            });
          }
        },
      );

      LogUtil.info('APK下载完成，文件路径: $filePath');

      // 检查文件是否存在
      final File apkFile = File(filePath);
      if (!await apkFile.exists()) {
        throw Exception('APK文件下载失败');
      }

      // 获取文件大小
      final fileSize = await apkFile.length();
      LogUtil.info('APK文件大小: ${fileSize} bytes');

      // 安装APK文件
      Get.snackbar('成功', 'APK下载完成，正在安装...');

      final result = await OpenFile.open(filePath);

      if (result.type == ResultType.done) {
        Get.snackbar('成功', 'APK安装已启动');
      } else {
        LogUtil.error('APK安装失败: ${result.message}');
        Get.snackbar('提示', '无法自动安装APK，请手动安装文件: $filePath');
      }
    } catch (e) {
      LogUtil.error('APK下载安装失败: $e');
      Get.snackbar('错误', 'APK下载安装失败: ${e.toString()}');
    } finally {
      setState(() {
        _isDownloadingApp = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('${'update_to_version'.tr} ${widget.version.version}'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // 版本信息卡片
            Card(
              child: Text(
                  widget.version.releaseNotes,
                  style: const TextStyle(fontSize: 14, height: 1.5),
              ),
            ),
            const SizedBox(height: 20),

            // 下载选项卡片
            Card(
              child: Container(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // 下载进度显示（应用内下载时显示）
                    if (_isDownloadingApp) ...[
                      Column(
                        children: [
                          // 进度条
                          LinearProgressIndicator(
                            value: _downloadProgress / 100,
                            backgroundColor: Colors.grey[300],
                            valueColor: AlwaysStoppedAnimation<Color>(
                              Colors.green,
                            ),
                            minHeight: 8,
                          ),
                          const SizedBox(height: 8),
                          // 进度百分比
                          Text(
                            '${'download_progress'.tr} ${_downloadProgress.toStringAsFixed(1)}%',
                            style: const TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.bold,
                              color: Colors.green,
                            ),
                          ),
                          const SizedBox(height: 16),
                        ],
                      ),
                    ],

                    // 下载按钮行
                    Row(
                      children: [
                        // 浏览器下载按钮
                        Expanded(
                          child: Column(
                            children: [
                              const Icon(
                                Icons.language,
                                size: 40,
                                color: Colors.blue,
                              ),
                              const SizedBox(height: 8),
                              Text(
                                'browser_download'.tr,
                                style: const TextStyle(
                                  fontSize: 14,
                                  fontWeight: FontWeight.bold,
                                ),
                                textAlign: TextAlign.center,
                              ),
                              const SizedBox(height: 8),
                              Text(
                                'download_via_browser'.tr,
                                style: const TextStyle(
                                  fontSize: 12,
                                  color: Colors.grey,
                                ),
                                textAlign: TextAlign.center,
                              ),
                              const SizedBox(height: 12),
                              ElevatedButton(
                                onPressed: _isDownloading
                                    ? null
                                    : _downloadUpdate,
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.blue,
                                  foregroundColor: Colors.white,
                                  minimumSize: const Size(double.infinity, 48),
                                ),
                                child: _isDownloading
                                    ? const SizedBox(
                                        width: 20,
                                        height: 20,
                                        child: CircularProgressIndicator(
                                          strokeWidth: 2,
                                          valueColor:
                                              AlwaysStoppedAnimation<Color>(
                                                Colors.white,
                                              ),
                                        ),
                                      )
                                    : Text('browser_download'.tr),
                              ),
                            ],
                          ),
                        ),

                        const SizedBox(width: 16),

                        // 应用内下载按钮
                        Expanded(
                          child: Column(
                            children: [
                              const Icon(
                                Icons.download,
                                size: 40,
                                color: Colors.green,
                              ),
                              const SizedBox(height: 8),
                              Text(
                                'in_app_download'.tr,
                                style: const TextStyle(
                                  fontSize: 14,
                                  fontWeight: FontWeight.bold,
                                ),
                                textAlign: TextAlign.center,
                              ),
                              const SizedBox(height: 8),
                              Text(
                                'download_and_install_in_app'.tr,
                                style: const TextStyle(
                                  fontSize: 12,
                                  color: Colors.grey,
                                ),
                                textAlign: TextAlign.center,
                              ),
                              const SizedBox(height: 12),
                              ElevatedButton(
                                onPressed: _isDownloadingApp
                                    ? null
                                    : _downloadAndInstallApp,
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.green,
                                  foregroundColor: Colors.white,
                                  minimumSize: const Size(double.infinity, 48),
                                ),
                                child: _isDownloadingApp
                                    ? const SizedBox(
                                        width: 20,
                                        height: 20,
                                        child: CircularProgressIndicator(
                                          strokeWidth: 2,
                                          valueColor:
                                              AlwaysStoppedAnimation<Color>(
                                                Colors.white,
                                              ),
                                        ),
                                      )
                                    : Text('in_app_download'.tr),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
