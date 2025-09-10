// 首先，我们需要导入额外的包
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import 'package:myappflutter/presentation/widgets/base_page.dart';
import 'dart:io';
import 'package:myappflutter/core/utils/prefs_util.dart';
import 'package:myappflutter/core/constants/prefs_constants.dart';

// 将StatelessWidget改为StatefulWidget
class ProfilePage extends StatefulWidget {
  const ProfilePage({super.key});

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  // 初始化HttpClient
  final HttpClient _httpClient = HttpClient();
  Map<String, String>? queryParameters;

  // 用户信息变量
  String _name = ''; // 姓名
  String _gender = ''; // 初始性别
  DateTime? _birthDate; // 初始出生日期
  String _avatarUrl = 'assets/images/user_null.png'; // 头像路径

  // 状态变量
  bool _isEditing = false; // 编辑状态
  bool _isLoading = false; // 加载状态
  bool _isUploading = false; // 上传状态

  TextEditingController _nameController = TextEditingController();
  final ImagePicker _picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    // 先初始化queryParameters
    PrefsUtil.getString(PrefsConstants.token).then((value) {
      queryParameters = {'Authorization': '$value'};
      // 然后再获取用户信息
      _fetchUserProfile();
    });
  }

  // 从后台获取用户信息
  Future<void> _fetchUserProfile() async {
    setState(() {
      _isLoading = true;
    });

    try {
      final response = await _httpClient.get('/profile');
      LogUtil.info('response: $response');

      // 解析响应数据
      setState(() {
        // 适配新格式，从data字段中获取数据
        final data = response['data'] ?? {};
        _name = data['username'] ?? '';
        _nameController.text = _name;
        _gender = data['gender'] ?? '';

        // 解析出生日期
        if (data['birthDate'] != null) {
          _birthDate = DateTime.parse(data['birthDate']);
        }

        // 解析头像URL
        if (data['avatarUrl'] != null && data['avatarUrl'].isNotEmpty) {
          _avatarUrl = _httpClient
              .buildUri('${data["avatarUrl"]}', queryParameters)
              .toString();
          LogUtil.info('头像URL: $_avatarUrl');
        }
      });
    } catch (e) {
      print('获取用户信息失败: $e');
      Get.snackbar('error'.tr, 'failed_to_fetch_user_info'.tr);
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  // 选择图片
  Future<void> _selectImage() async {
    final XFile? image = await _picker.pickImage(
      source: ImageSource.gallery,
      imageQuality: 80,
    );

    if (image != null) {
      // 上传图片
      _uploadImage(File(image.path));
    }
  }

  // 拍照
  Future<void> _takePhoto() async {
    final XFile? image = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );

    if (image != null) {
      // 上传图片
      _uploadImage(File(image.path));
    }
  }

  // 上传图片
  Future<void> _uploadImage(File imageFile) async {
    setState(() {
      _isUploading = true;
    });

    try {
      // 调用HttpClient的上传方法
      final response = await _httpClient.uploadFile(
        '/images/upload',
        imageFile,
      );

      // 适配新的数据格式
      if (response['code'] == 200) {
        // 提取data字段
        final data = response['data'] ?? {};
        final fileId = data['fileId'] ?? '';
        final fileName = data['fileName'] ?? '';

        // 这里可以根据实际需求使用fileId和fileName
        // 例如，更新用户头像
        setState(() {
          // 假设后端提供了一个获取图片的URL格式
          _avatarUrl = _httpClient
              .buildUri('/images/item/$fileName', queryParameters)
              .toString();
          LogUtil.info('上传成功，_avatarUrl: $_avatarUrl');
        });

        Get.snackbar('success'.tr, 'avatar_updated'.tr);
        LogUtil.info('上传成功，文件ID: $fileId, 文件名: $fileName');
      } else {
        Get.snackbar('error'.tr, 'avatar_update_failed'.tr);
      }
    } catch (e) {
      print('上传头像失败: $e');
      Get.snackbar('error'.tr, 'avatar_upload_failed'.tr);
    } finally {
      setState(() {
        _isUploading = false;
      });
    }
  }

  // 显示选择对话框
  void _showImageSourceDialog() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('select_image_source'.tr),
          content: SingleChildScrollView(
            child: ListBody(
              children: <Widget>[
                GestureDetector(
                  child: Text('select_from_gallery'.tr),
                  onTap: () {
                    Navigator.of(context).pop();
                    _selectImage();
                  },
                ),
                const Padding(padding: EdgeInsets.all(8.0)),
                GestureDetector(
                  child: Text('take_photo'.tr),
                  onTap: () {
                    Navigator.of(context).pop();
                    _takePhoto();
                  },
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'profile'.tr,
      body: Center(
        child: SingleChildScrollView(
          child: Center(
            child: Column(
              children: [
                Card(
                  elevation: 8,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(100),
                  ),
                  child: GestureDetector(
                    onTap: _showImageSourceDialog,
                    child: CircleAvatar(
                      radius: 60,
                      backgroundColor: Theme.of(context).primaryColor,
                      child: CircleAvatar(
                        radius: 55,
                        backgroundImage: _avatarUrl.startsWith('http')
                            ? NetworkImage(_avatarUrl)
                            : AssetImage(_avatarUrl) as ImageProvider,
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 30),
                Card(
                  elevation: 4,
                  margin: const EdgeInsets.symmetric(horizontal: 20),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(15),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(20),
                    child: Column(
                      children: [
                        _buildNameItem(),
                        const Divider(),
                        _buildGenderItem(),
                        const Divider(),
                        _buildBirthDateItem(),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 30),
                ElevatedButton(
                  onPressed: _isLoading ? null : _toggleEditing,
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 40,
                      vertical: 15,
                    ),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(30),
                    ),
                  ),
                  child: Text(
                    _isEditing ? 'save_changes'.tr : 'edit_profile'.tr,
                    style: const TextStyle(fontSize: 16),
                  ),
                ),

                const SizedBox(height: 30),
              ],
            ),
          ),
        ),
      ),
    );
  }

  // 构建姓名项
  Widget _buildNameItem() {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10),
      child: Row(
        children: [
          Icon(Icons.person, color: Theme.of(context).primaryColor),
          const SizedBox(width: 15),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'name'.tr,
                style: TextStyle(fontSize: 14, color: Colors.grey[600]),
              ),
              const SizedBox(height: 5),
              if (_isEditing) ...[
                SizedBox(
                  width: 200,
                  child: TextField(
                    controller: _nameController,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                    ),
                  ),
                ),
              ] else ...[
                Text(
                  _name,
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ],
          ),
        ],
      ),
    );
  }

  // 构建性别选择项
  Widget _buildGenderItem() {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10),
      child: Row(
        children: [
          Icon(Icons.male, color: Theme.of(context).primaryColor),
          const SizedBox(width: 15),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'gender'.tr,
                style: TextStyle(fontSize: 14, color: Colors.grey[600]),
              ),
              const SizedBox(height: 5),
              if (_isEditing) ...[
                Row(
                  children: [
                    Radio(
                      value: 'male',
                      groupValue: _gender,
                      onChanged: (value) {
                        setState(() {
                          _gender = value!;
                        });
                      },
                    ),
                    Text('male'.tr),
                    Radio(
                      value: 'female',
                      groupValue: _gender,
                      onChanged: (value) {
                        setState(() {
                          _gender = value!;
                        });
                      },
                    ),
                    Text('female'.tr),
                  ],
                ),
              ] else ...[
                Text(
                  _gender == 'male'
                      ? 'male'.tr
                      : (_gender == 'female' ? 'female'.tr : ''),
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ],
          ),
        ],
      ),
    );
  }

  // 构建出生日期项
  Widget _buildBirthDateItem() {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10),
      child: Row(
        children: [
          Icon(Icons.calendar_today, color: Theme.of(context).primaryColor),
          const SizedBox(width: 15),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'birth_date'.tr,
                style: TextStyle(fontSize: 14, color: Colors.grey[600]),
              ),
              const SizedBox(height: 5),
              if (_isEditing) ...[
                ElevatedButton(
                  onPressed: () async {
                    final DateTime? picked = await showDatePicker(
                      context: context,
                      initialDate: _birthDate ?? DateTime.now(),
                      firstDate: DateTime(1900),
                      lastDate: DateTime.now(),
                    );
                    if (picked != null) {
                      setState(() {
                        _birthDate = picked;
                      });
                    }
                  },
                  child: Text(
                    _birthDate == null
                        ? 'select_birth_date'.tr
                        : '${_birthDate!.year}-${_birthDate!.month}-${_birthDate!.day}',
                  ),
                ),
              ] else ...[
                Text(
                  _birthDate == null
                      ? ''
                      : '${_birthDate!.year}-${_birthDate!.month}-${_birthDate!.day}',
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ],
          ),
        ],
      ),
    );
  }

  // 切换编辑状态
  void _toggleEditing() {
    if (_isEditing) {
      // 保存修改
      _saveUserInfo();
    } else {
      setState(() {
        _isEditing = true;
      });
    }
  }

  // 保存用户信息
  void _saveUserInfo() async {
    setState(() {
      _isLoading = true;
    });

    try {
      // 准备请求参数
      final Map<String, dynamic> params = {
        'username': _nameController.text,
        'gender': _gender,
      };

      // 如果出生日期不为空，则添加到参数中
      if (_birthDate != null) {
        params['birthDate'] =
            '${_birthDate!.year}-${_birthDate!.month}-${_birthDate!.day}';
      }

      // 发送请求
      final response = await _httpClient.post('/updateUserInfo', body: params);

      if (response['code'] == 200) {
        Get.snackbar('update_success'.tr, 'user_info_updated'.tr);
        setState(() {
          _isEditing = false;
          _name = _nameController.text;
        });
      } else {
        Get.snackbar(
          'update_failed'.tr,
          response['message'] ?? 'update_failed'.tr,
        );
      }
    } catch (e) {
      print('更新用户信息失败: $e');
      Get.snackbar('update_failed'.tr, 'network_exception'.tr);
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }
}
