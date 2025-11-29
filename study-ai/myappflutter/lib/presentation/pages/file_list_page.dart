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

  // 分页和搜索相关状态
  int _currentPage = 1;
  int _pageSize = 10;
  int _totalItems = 0;
  TextEditingController _searchController = TextEditingController();
  String _searchKeyword = '';

  @override
  void initState() {
    super.initState();
    _loadFileList();
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  Future<void> _loadFileList() async {
    setState(() {
      _isLoading = true;
    });

    try {
      // 构建请求参数
      final Map<String, dynamic> queryParams = {
        'page': _currentPage,
        'pageSize': _pageSize,
        'keyword': '',
      };

      // 如果有搜索关键词，添加到参数中
      if (_searchKeyword.isNotEmpty) {
        queryParams['keyword'] = _searchKeyword;
      }

      // 调用获取文件列表的API（带分页和搜索参数）
      final response = await _httpClient.post(
        '/app/file/list',
        body: queryParams,
      );

      if (response['code'] == 200 && response['data'] is Map) {
        final data = response['data'] as Map;
        if (data['records'] is List) {
          setState(() {
            _fileList = (data['records'] as List)
                .map((item) => FileInfo.fromJson(item))
                .toList();
            _totalItems = data['total'] ?? 0;
          });
        }
      }
    } catch (e) {
      LogUtil.error('加载文件列表失败: $e');
      Get.snackbar('错误', '加载文件列表失败，请重试');
      // 如果API调用失败，使用模拟数据（适配分页）
      _loadMockFileList();
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  // 搜索功能
  void _onSearch() {
    setState(() {
      _searchKeyword = _searchController.text.trim();
      _currentPage = 1; // 搜索时重置到第一页
    });
    _loadFileList();
  }

  // 清除搜索
  void _clearSearch() {
    setState(() {
      _searchController.clear();
      _searchKeyword = '';
      _currentPage = 1;
    });
    _loadFileList();
  }

  // 切换到上一页
  void _goToPreviousPage() {
    if (_currentPage > 1) {
      setState(() {
        _currentPage--;
      });
      _loadFileList();
    }
  }

  // 切换到下一页
  void _goToNextPage() {
    if (_fileList.length >= _pageSize) {
      setState(() {
        _currentPage++;
      });
      _loadFileList();
    }
  }

  // 跳转到指定页码
  void _goToPage(int page) {
    setState(() {
      _currentPage = page;
    });
    _loadFileList();
  }

  void _loadMockFileList() {
    // 模拟文件数据（更多数据用于测试分页）
    final mockFiles = [];
    for (int i = 1; i <= 50; i++) {
      // 如果有搜索关键词，只返回包含关键词的文件
      if (_searchKeyword.isNotEmpty &&
          !('文档_$i'.contains(_searchKeyword) ||
              '图片_$i'.contains(_searchKeyword) ||
              '手册_$i'.contains(_searchKeyword))) {
        continue;
      }

      mockFiles.add({
        'id': i,
        'fileName':
            'uuid_${i % 3 == 0
                ? '文档'
                : i % 3 == 1
                ? '图片'
                : '手册'}_$i.${i % 3 == 0
                ? 'pdf'
                : i % 3 == 1
                ? 'jpg'
                : 'docx'}',
        'originalFileName':
            '${i % 3 == 0
                ? '示例文档'
                : i % 3 == 1
                ? '产品图片'
                : '开发手册'}_$i.${i % 3 == 0
                ? 'pdf'
                : i % 3 == 1
                ? 'jpg'
                : 'docx'}',
        'fileSize': 1024 * i,
        'fileUrl': '/file/download/uuid_文件_$i',
        'fileType': i % 3 == 0
            ? 'application/pdf'
            : i % 3 == 1
            ? 'image/jpeg'
            : 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'createdTime': '2024-0${i % 12 + 1}-${(i % 28) + 1}T10:00:00',
        'updatedTime': '2024-0${i % 12 + 1}-${(i % 28) + 1}T10:00:00',
      });
    }

    // 模拟分页
    final startIndex = (_currentPage - 1) * _pageSize;
    final endIndex = startIndex + _pageSize;
    List filteredFiles = [];

    if (startIndex < mockFiles.length) {
      filteredFiles = mockFiles.sublist(
        startIndex,
        endIndex > mockFiles.length ? mockFiles.length : endIndex,
      );
    }

    setState(() {
      _fileList = filteredFiles.map((item) => FileInfo.fromJson(item)).toList();
      _totalItems = mockFiles.length;
    });
  }

  // 构建分页控件
  Widget _buildPagination() {
    final totalPages = (_totalItems + _pageSize - 1) ~/ _pageSize;

    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border(top: BorderSide(color: Colors.grey.shade200)),
      ),
      child: Column(
        children: [
          // 显示总数和当前页信息
          Text(
            '${'total'.tr}: $_totalItems ${'items'.tr}, ${'page'.tr}: $_currentPage/$totalPages',
            style: const TextStyle(fontSize: 14, color: Colors.grey),
          ),
          const SizedBox(height: 12),
          // 分页按钮
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              // 上一页按钮
              ElevatedButton(
                onPressed: _currentPage > 1 ? _goToPreviousPage : null,
                style: ElevatedButton.styleFrom(
                  backgroundColor: _currentPage > 1
                      ? Colors.blue
                      : Colors.grey.shade300,
                ),
                child: Text('${'previous'.tr}'),
              ),
              const SizedBox(width: 8),

              // 页码按钮（最多显示5个页码）
              Row(children: _buildPageButtons(totalPages)),
              const SizedBox(width: 8),

              // 下一页按钮
              ElevatedButton(
                onPressed: _fileList.length >= _pageSize ? _goToNextPage : null,
                style: ElevatedButton.styleFrom(
                  backgroundColor: _fileList.length >= _pageSize
                      ? Colors.blue
                      : Colors.grey.shade300,
                ),
                child: Text('${'next'.tr}'),
              ),
            ],
          ),
        ],
      ),
    );
  }

  // 构建页码按钮列表
  List<Widget> _buildPageButtons(int totalPages) {
    final List<Widget> buttons = [];
    final int maxVisiblePages = 5;
    int startPage = 1;
    int endPage = totalPages;

    if (totalPages > maxVisiblePages) {
      startPage = _currentPage - (maxVisiblePages ~/ 2);
      endPage = _currentPage + (maxVisiblePages ~/ 2);

      if (startPage < 1) {
        startPage = 1;
        endPage = maxVisiblePages;
      } else if (endPage > totalPages) {
        endPage = totalPages;
        startPage = endPage - maxVisiblePages + 1;
      }
    }

    // 添加省略号（如果需要）
    if (startPage > 1) {
      buttons.add(
        TextButton(onPressed: () => _goToPage(1), child: const Text('1')),
      );
      if (startPage > 2) {
        buttons.add(const Text('...'));
      }
    }

    // 添加页码按钮
    for (int i = startPage; i <= endPage; i++) {
      buttons.add(
        ElevatedButton(
          onPressed: () => _goToPage(i),
          style: ElevatedButton.styleFrom(
            backgroundColor: i == _currentPage
                ? Colors.blue.shade700
                : Colors.grey.shade200,
            foregroundColor: i == _currentPage ? Colors.white : Colors.black,
          ),
          child: Text('$i'),
        ),
      );
    }

    // 添加省略号（如果需要）
    if (endPage < totalPages) {
      if (endPage < totalPages - 1) {
        buttons.add(const Text('...'));
      }
      buttons.add(
        TextButton(
          onPressed: () => _goToPage(totalPages),
          child: Text('$totalPages'),
        ),
      );
    }

    return buttons;
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

      Get.snackbar(
        'success'.tr,
        '${'download_success'.tr}\n${'save_path'.tr}: $savePath',
      );
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

  // 文件名中间省略处理函数
  String _truncateFileName(String fileName, {int maxLength = 80}) {
    if (fileName.length <= maxLength) {
      return fileName;
    }

    final int middleLength = 3; // 中间省略号占用的长度
    final int partLength = (maxLength - middleLength) ~/ 2;

    // 保留文件名后缀
    final int lastDotIndex = fileName.lastIndexOf('.');
    if (lastDotIndex != -1 && lastDotIndex > fileName.length * 0.7) {
      // 如果后缀点在文件名的70%位置之后，优先保留后缀
      final String extension = fileName.substring(lastDotIndex);
      if (extension.length < maxLength * 0.3) {
        final int availableLength = maxLength - extension.length - middleLength;
        return '${fileName.substring(0, availableLength)}...$extension';
      }
    }

    // 普通情况：前后各保留一部分，中间省略
    return '${fileName.substring(0, partLength)}...${fileName.substring(fileName.length - partLength)}';
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
          ? Container(
              color: Colors.white.withOpacity(0.95),
              child: Center(
                child: Card(
                  elevation: 8,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      const CircularProgressIndicator(
                        strokeWidth: 4,
                        valueColor: AlwaysStoppedAnimation<Color>(Colors.blue),
                      ),
                      const SizedBox(height: 20),
                      Text(
                        'loading_files'.tr,
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w500,
                          color: Colors.black87,
                        ),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        'please_wait'.tr,
                        style: const TextStyle(
                          fontSize: 14,
                          color: Colors.grey,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            )
          : Column(
              children: [
                // 搜索框
                Padding(
                  padding: const EdgeInsets.all(16),
                  child: TextField(
                    controller: _searchController,
                    decoration: InputDecoration(
                      labelText: 'search_file'.tr,
                      hintText: 'enter_file_name'.tr,
                      prefixIcon: const Icon(Icons.search),
                      suffixIcon: _searchKeyword.isNotEmpty
                          ? IconButton(
                              icon: const Icon(Icons.clear),
                              onPressed: _clearSearch,
                            )
                          : null,
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                    ),
                    onSubmitted: (value) => _onSearch(),
                  ),
                ),

                // 搜索按钮
                Padding(
                  padding: const EdgeInsets.symmetric(
                    horizontal: 16,
                    vertical: 8,
                  ),
                  child: ElevatedButton(
                    onPressed: _onSearch,
                    style: ElevatedButton.styleFrom(
                      minimumSize: const Size(double.infinity, 40),
                    ),
                    child: Text('search'.tr),
                  ),
                ),

                // 文件列表
                _fileList.isEmpty
                    ? Expanded(child: Center(child: Text('no_files'.tr)))
                    : Expanded(
                        child: ListView.builder(
                          itemCount: _fileList.length,
                          itemBuilder: (context, index) {
                            final file = _fileList[index];
                            final isDownloading =
                                _isDownloading[file.id] ?? false;
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
                                    // 文件名单独显示，超出长度时中间使用省略号
                                    Text(
                                      _truncateFileName(file.displayName),
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
                                            valueColor:
                                                const AlwaysStoppedAnimation<
                                                  Color
                                                >(Colors.blue),
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
                                          minimumSize: const Size(
                                            double.infinity,
                                            40,
                                          ),
                                        ),
                                        child: Text('download_file'.tr),
                                      ),
                                  ],
                                ),
                              ),
                            );
                          },
                        ),
                      ),

                // 分页控件
                _buildPagination(),
              ],
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
      originalFileName:
          json['originalFileName']?.toString() ??
          json['fileName']?.toString() ??
          '未知文件',
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
