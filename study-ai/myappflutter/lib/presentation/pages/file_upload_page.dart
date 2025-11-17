import 'dart:io';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import 'package:myappflutter/presentation/widgets/base_page.dart';

class FileUploadPage extends StatefulWidget {
  const FileUploadPage({super.key});

  @override
  State<FileUploadPage> createState() => _FileUploadPageState();
}

class _FileUploadPageState extends State<FileUploadPage> {
  final HttpClient _httpClient = HttpClient();
  final ImagePicker _imagePicker = ImagePicker();

  File? _selectedFile;
  bool _isUploading = false;
  double _uploadProgress = 0.0;
  String? _uploadResult;

  Future<void> _pickFile() async {
    try {
      final XFile? pickedFile = await _imagePicker.pickImage(
        source: ImageSource.gallery,
        maxWidth: 1920,
        maxHeight: 1080,
        imageQuality: 85,
      );

      if (pickedFile != null) {
        setState(() {
          _selectedFile = File(pickedFile.path);
          _uploadResult = null;
        });
      }
    } catch (e) {
      LogUtil.error('选择文件失败: $e');
      Get.snackbar('error'.tr, '${'file_upload_failed'.tr}, ${'retry'.tr}');
    }
  }

  Future<void> _uploadFile() async {
    if (_selectedFile == null) {
      Get.snackbar('prompt'.tr, 'please_select_file_first'.tr);
      return;
    }

    setState(() {
      _isUploading = true;
      _uploadProgress = 0.0;
      _uploadResult = null;
    });

    try {
      final response = await _httpClient.uploadFile(
        '/app/file/upload', // 后端上传接口路径
        _selectedFile!,
        fieldName: 'file',
        onReceiveProgress: (received, total) {
          if (total > 0) {
            setState(() {
              _uploadProgress = received / total;
            });
          }
        },
      );

      if (response['code'] == 200) {
        setState(() {
          _uploadResult = 'file_upload_success'.tr;
        });
        Get.snackbar('success'.tr, 'file_upload_success'.tr);
      } else {
        throw Exception(response['message'] ?? '上传失败');
      }
    } catch (e) {
      LogUtil.error('文件上传失败: $e');
      setState(() {
        _uploadResult = '${'file_upload_failed'.tr}: $e';
      });
      Get.snackbar('error'.tr, '${'file_upload_failed'.tr}, ${'retry'.tr}');
    } finally {
      setState(() {
        _isUploading = false;
      });
    }
  }

  void _clearSelection() {
    setState(() {
      _selectedFile = null;
      _uploadResult = null;
      _uploadProgress = 0.0;
    });
  }

  Widget _buildFileInfo() {
    if (_selectedFile == null) {
      return const SizedBox.shrink();
    }

    return Card(
      margin: const EdgeInsets.all(16.0),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              'selected_file_info'.tr,
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Text('${'file_name'.tr}: ${_selectedFile!.path.split('/').last}'),
            Text(
              '${'file_size'.tr}: ${(_selectedFile!.lengthSync() / 1024).toStringAsFixed(2)} KB',
            ),
            Text('${'file_path'.tr}: ${_selectedFile!.path}'),
            const SizedBox(height: 16),
            Row(
              children: [
                Expanded(
                  child: ElevatedButton.icon(
                    onPressed: _clearSelection,
                    icon: const Icon(Icons.clear),
                    label: Text('clear_selection'.tr),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.grey,
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: ElevatedButton.icon(
                    onPressed: _uploadFile,
                    icon: const Icon(Icons.cloud_upload),
                    label: Text('upload_file'.tr),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildProgressIndicator() {
    if (!_isUploading) return const SizedBox.shrink();

    return Card(
      margin: const EdgeInsets.all(16.0),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Text(
              'upload_progress'.tr,
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            LinearProgressIndicator(
              value: _uploadProgress,
              backgroundColor: Colors.grey[300],
              valueColor: const AlwaysStoppedAnimation<Color>(Colors.blue),
            ),
            const SizedBox(height: 8),
            Text('${(_uploadProgress * 100).toStringAsFixed(1)}%'),
          ],
        ),
      ),
    );
  }

  Widget _buildResult() {
    if (_uploadResult == null) return const SizedBox.shrink();

    return Card(
      margin: const EdgeInsets.all(16.0),
      color: _uploadResult!.contains('成功') ? Colors.green[50] : Colors.red[50],
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Text(
          _uploadResult!,
          style: TextStyle(
            color: _uploadResult!.contains('成功') ? Colors.green : Colors.red,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'file_upload'.tr,
      body: SingleChildScrollView(
        child: Column(
          children: [
            // 选择文件区域
            Card(
              margin: const EdgeInsets.all(16.0),
              child: Padding(
                padding: const EdgeInsets.all(24.0),
                child: Column(
                  children: [
                    Icon(Icons.cloud_upload, size: 64, color: Colors.blue),
                    const SizedBox(height: 16),
                    Text(
                      'select_file_to_upload'.tr,
                      style: TextStyle(
                        fontSize: 18,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'supports_images_documents'.tr,
                      style: TextStyle(color: Colors.grey),
                    ),
                    const SizedBox(height: 24),
                    ElevatedButton.icon(
                      onPressed: _pickFile,
                      icon: const Icon(Icons.photo_library),
                      label: Text('select_file'.tr),
                      style: ElevatedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 24,
                          vertical: 12,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),

            // 文件信息
            _buildFileInfo(),

            // 上传进度
            _buildProgressIndicator(),

            // 上传结果
            _buildResult(),
          ],
        ),
      ),
    );
  }
}
