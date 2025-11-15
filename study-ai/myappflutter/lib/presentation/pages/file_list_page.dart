import 'dart:io';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:path_provider/path_provider.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import 'package:myappflutter/presentation/widgets/base_page.dart';

class FileListPage extends StatefulWidget {
  const FileListPage({super.key});

  @override
  State<FileListPage> createState() => _FileListPageState();
}

class _FileListPageState extends State<FileListPage> {
  final HttpClient _httpClient = HttpClient();
  List<FileInfo> _fileList = [];
  bool _isLoading = false;
  Map<String, double> _downloadProgress = {};
  Map<String, bool> _isDownloading = {};

  @override
  void initState() {
    super.initState();
    _loadFileList();
  }

  Future<void> _loadFileList() async {
    setState(() {
      _isLoading = true;
    });

    try {
      // 调用获取文件列表的API
      final response = await _httpClient.get('/app/versions');

      if (response['code'] == 200 && response['data'] is List) {
        setState(() {
          _fileList = (response['data'] as List)
              .map((item) => FileInfo.fromJson(item))
              .toList();
        });
      }
    } catch (e) {
      LogUtil.error('加载文件列表失败: $e');
      Get.snackbar('错误', '加载文件列表失败，请重试');
      // 如果API调用失败，使用模拟数据
      _loadMockFileList();
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  void _loadMockFileList() {
    // 模拟文件数据
    final mockFiles = [
      {
        'id': '1',
        'fileName': '示例文档.pdf',
        'fileSize': 2048,
        'uploadTime': '2024-01-01 10:00:00',
        'fileUrl': '/app/download/1',
      },
      {
        'id': '2',
        'fileName': '产品图片.jpg',
        'fileSize': 1024,
        'uploadTime': '2024-01-02 14:30:00',
        'fileUrl': '/app/download/2',
      },
      {
        'id': '3',
        'fileName': '开发手册.docx',
        'fileSize': 4096,
        'uploadTime': '2024-01-03 09:15:00',
        'fileUrl': '/app/download/3',
      },
    ];

    setState(() {
      _fileList = mockFiles.map((item) => FileInfo.fromJson(item)).toList();
    });
  }

  Future<void> _downloadFile(FileInfo file) async {
    setState(() {
      _isDownloading[file.id] = true;
      _downloadProgress[file.id] = 0.0;
    });

    try {
      // 获取下载目录
      final directory = await getExternalStorageDirectory();
      if (directory == null) {
        throw Exception('无法获取存储目录');
      }

      final savePath = '${directory.path}/${file.fileName}';

      // 下载文件
      await _httpClient.download(
        file.fileUrl,
        savePath,
        onReceiveProgress: (received, total) {
          if (total > 0) {
            setState(() {
              _downloadProgress[file.id] = received / total;
            });
          }
        },
      );

      Get.snackbar('成功', '文件下载成功\n保存路径: $savePath');
    } catch (e) {
      LogUtil.error('文件下载失败: $e');
      Get.snackbar('错误', '文件下载失败: $e');
    } finally {
      setState(() {
        _isDownloading[file.id] = false;
      });
    }
  }

  String _formatFileSize(int bytes) {
    if (bytes < 1024)
      return '$bytes B';
    else if (bytes < 1024 * 1024)
      return '${(bytes / 1024).toStringAsFixed(1)} KB';
    else
      return '${(bytes / (1024 * 1024)).toStringAsFixed(1)} MB';
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: '文件列表',
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _fileList.isEmpty
          ? const Center(child: Text('暂无文件'))
          : ListView.builder(
              itemCount: _fileList.length,
              itemBuilder: (context, index) {
                final file = _fileList[index];
                final isDownloading = _isDownloading[file.id] ?? false;
                final progress = _downloadProgress[file.id] ?? 0.0;

                return Card(
                  margin: const EdgeInsets.symmetric(
                    horizontal: 16,
                    vertical: 8,
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Expanded(
                              child: Text(
                                file.fileName,
                                style: const TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                ),
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                              ),
                            ),
                            Text(
                              _formatFileSize(file.fileSize),
                              style: const TextStyle(
                                color: Colors.grey,
                                fontSize: 14,
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        Text(
                          '上传时间: ${file.uploadTime}',
                          style: const TextStyle(
                            color: Colors.grey,
                            fontSize: 14,
                          ),
                        ),
                        const SizedBox(height: 12),
                        if (isDownloading)
                          Column(
                            children: [
                              LinearProgressIndicator(
                                value: progress,
                                backgroundColor: Colors.grey[200],
                                valueColor: const AlwaysStoppedAnimation<Color>(
                                  Colors.blue,
                                ),
                              ),
                              const SizedBox(height: 8),
                              Text(
                                '下载中: ${(progress * 100).toStringAsFixed(0)}%',
                                style: const TextStyle(
                                  color: Colors.blue,
                                  fontSize: 14,
                                ),
                              ),
                            ],
                          )
                        else
                          ElevatedButton(
                            onPressed: () => _downloadFile(file),
                            style: ElevatedButton.styleFrom(
                              minimumSize: const Size(double.infinity, 40),
                            ),
                            child: const Text('下载文件'),
                          ),
                      ],
                    ),
                  ),
                );
              },
            ),
    );
  }
}

class FileInfo {
  final String id;
  final String fileName;
  final int fileSize;
  final String uploadTime;
  final String fileUrl;

  FileInfo({
    required this.id,
    required this.fileName,
    required this.fileSize,
    required this.uploadTime,
    required this.fileUrl,
  });

  factory FileInfo.fromJson(Map<String, dynamic> json) {
    return FileInfo(
      id: json['id']?.toString() ?? '',
      fileName: json['fileName']?.toString() ?? '未知文件',
      fileSize: json['fileSize'] ?? 0,
      uploadTime: json['uploadTime']?.toString() ?? '',
      fileUrl: json['fileUrl']?.toString() ?? '',
    );
  }
}
