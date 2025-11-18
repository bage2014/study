import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/config/app_routes.dart';
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
      final response = await _httpClient.get('/app/file/list');

      if (response['code'] == 200 && response['data'] is Map) {
        final data = response['data'] as Map;
        if (data['files'] is List) {
          setState(() {
            _fileList = (data['files'] as List)
                .map((item) => FileInfo.fromJson(item))
                .toList();
          });
        }
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
        'id': 1,
        'fileName': 'uuid_示例文档.pdf',
        'originalFileName': '示例文档.pdf',
        'fileSize': 2048,
        'fileUrl': '/file/download/uuid_示例文档.pdf',
        'fileType': 'application/pdf',
        'createdTime': '2024-01-01T10:00:00',
        'updatedTime': '2024-01-01T10:00:00',
      },
      {
        'id': 2,
        'fileName': 'uuid_产品图片.jpg',
        'originalFileName': '产品图片.jpg',
        'fileSize': 1024,
        'fileUrl': '/file/download/uuid_产品图片.jpg',
        'fileType': 'image/jpeg',
        'createdTime': '2024-01-02T14:30:00',
        'updatedTime': '2024-01-02T14:30:00',
      },
      {
        'id': 3,
        'fileName': 'uuid_开发手册.docx',
        'originalFileName': '开发手册.docx',
        'fileSize': 4096,
        'fileUrl': '/file/download/uuid_开发手册.docx',
        'fileType': 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'createdTime': '2024-01-03T09:15:00',
        'updatedTime': '2024-01-03T09:15:00',
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

      // 使用原始文件名保存下载的文件
      final savePath = '${directory.path}/${file.displayName}';

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

      Get.snackbar('success'.tr, '${'download_success'.tr}\n${'save_path'.tr}: $savePath');
    } catch (e) {
      LogUtil.error('文件下载失败: $e');
      Get.snackbar('error'.tr, '${'download_failed'.tr}: $e');
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
      title: 'file_list'.tr,
      actions: [
        IconButton(
          icon: const Icon(Icons.upload),
          onPressed: () => Get.toNamed(AppRoutes.FILE_UPLOAD),
          tooltip: 'upload_file'.tr,
        ),
      ],
      body: _isLoading
          ? Center(child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const CircularProgressIndicator(),
                const SizedBox(height: 16),
                Text('loading_files'.tr),
              ],
            ))
          : _fileList.isEmpty
          ? Center(child: Text('no_files'.tr))
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
                        // 文件名单独显示
                        Text(
                          file.displayName,
                          style: const TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                        const SizedBox(height: 4),
                        // 文件大小放在下方
                        Text(
                          _formatFileSize(file.fileSize),
                          style: const TextStyle(
                            color: Colors.grey,
                            fontSize: 14,
                          ),
                        ),
                        const SizedBox(height: 4),
                        // 上传时间
                        Text(
                          '${'upload_time'.tr}: ${file.uploadTime}',
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
                                '${'downloading'.tr}: ${(progress * 100).toStringAsFixed(0)}%',
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
                            child: Text('download_file'.tr),
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
  final String originalFileName;
  final int fileSize;
  final String createdTime;
  final String fileUrl;
  final String fileType;

  FileInfo({
    required this.id,
    required this.fileName,
    required this.originalFileName,
    required this.fileSize,
    required this.createdTime,
    required this.fileUrl,
    required this.fileType,
  });

  factory FileInfo.fromJson(Map<String, dynamic> json) {
    return FileInfo(
      id: json['id']?.toString() ?? '',
      fileName: json['fileName']?.toString() ?? '未知文件',
      originalFileName: json['originalFileName']?.toString() ?? json['fileName']?.toString() ?? '未知文件',
      fileSize: json['fileSize'] ?? 0,
      createdTime: json['createdTime']?.toString() ?? '',
      fileUrl: json['fileUrl']?.toString() ?? '',
      fileType: json['fileType']?.toString() ?? 'application/octet-stream',
    );
  }

  // 获取显示用的上传时间（格式化ISO时间）
  String get uploadTime {
    try {
      if (createdTime.contains('T')) {
        final parts = createdTime.split('T');
        return '${parts[0]} ${parts[1].split('.')[0]}';
      }
      return createdTime;
    } catch (e) {
      return createdTime;
    }
  }

  // 获取显示用的文件名（优先使用原始文件名）
  String get displayName {
    return originalFileName.isNotEmpty ? originalFileName : fileName;
  }
}
