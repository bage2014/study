import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/config/app_routes.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../widgets/base_page.dart';
import 'dart:io';
import 'package:image_picker/image_picker.dart';
import 'package:myappflutter/core/constants/prefs_constants.dart';
import 'package:myappflutter/core/utils/prefs_util.dart';

class FamilyEditPage extends StatefulWidget {
  const FamilyEditPage({super.key});

  @override
  State<FamilyEditPage> createState() => _FamilyEditPageState();
}

class _FamilyEditPageState extends State<FamilyEditPage> {
  String? _selectedRelationship;
  final List<String> _relationshipOptions = [
    'PARENT_CHILD',
    'SPOUSE',
    'SIBLING',
    'GRANDPARENT_GRANDCHILD',
    'FATHER',
    'MOTHER',
    'SON',
    'DAUGHTER',
    'HUSBAND',
    'WIFE',
    'BROTHER',
    'SISTER',
    'GRANDFATHER',
    'GRANDMOTHER',
    'GRANDSON',
    'GRANDDAUGHTER',
    'UNCLE',
    'AUNT',
    'NEPHEW',
    'NIECE',
    'OTHER',
  ];

  // 新增：选中的用户信息
  Map<String, dynamic>? _selectedUser;

  // 新增：是否添加新成员模式
  bool _isAddNewMemberMode = false;

  // 新增：新成员表单控制器
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _birthPlaceController = TextEditingController();
  final TextEditingController _occupationController = TextEditingController();
  final TextEditingController _educationController = TextEditingController();
  final TextEditingController _contactInfoController = TextEditingController();
  String _gender = 'MALE';
  bool _deceased = false;
  int _generation = 0;
  DateTime? _birthDate;
  DateTime? _deathDate;
  File? _avatarImage;

  // 创建HttpClient实例
  final HttpClient _httpClient = HttpClient();

  @override
  void dispose() {
    _nameController.dispose();
    _birthPlaceController.dispose();
    _occupationController.dispose();
    _educationController.dispose();
    _contactInfoController.dispose();
    super.dispose();
  }

  // 新增：切换添加模式
  void _toggleAddMode() {
    setState(() {
      _isAddNewMemberMode = !_isAddNewMemberMode;
      // 重置表单
      if (_isAddNewMemberMode) {
        _selectedUser = null;
      } else {
        _resetForm();
      }
    });
  }

  // 新增：重置表单
  void _resetForm() {
    _nameController.clear();
    _birthPlaceController.clear();
    _occupationController.clear();
    _educationController.clear();
    _contactInfoController.clear();
    _gender = 'MALE';
    _deceased = false;
    _generation = 0;
    _birthDate = null;
    _deathDate = null;
    _avatarImage = null;
  }

  // 新增：选择头像
  Future<void> _pickImage() async {
    final picker = ImagePicker();
    final pickedFile = await picker.pickImage(source: ImageSource.gallery);

    if (pickedFile != null) {
      setState(() {
        _avatarImage = File(pickedFile.path);
      });
    }
  }

  // 新增：选择日期
  Future<void> _selectDate(BuildContext context, bool isBirthDate) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1900),
      lastDate: DateTime.now(),
    );

    if (picked != null) {
      setState(() {
        if (isBirthDate) {
          _birthDate = picked;
        } else {
          _deathDate = picked;
        }
      });
    }
  }

  // 新增：添加新成员
  Future<void> _addNewMember() async {
    try {
      // 验证必填项
      if (_nameController.text.isEmpty) {
        Get.snackbar('error'.tr, 'name_required'.tr);
        return;
      }

      // 构建请求参数
      final requestBody = {
        'name': _nameController.text.trim(),
        'gender': _gender,
        'birthDate': _birthDate?.toIso8601String().split('T')[0],
        'birthPlace': _birthPlaceController.text.trim(),
        'occupation': _occupationController.text.trim(),
        'education': _educationController.text.trim(),
        'contactInfo': _contactInfoController.text.trim(),
        'deceased': _deceased,
        'deathDate': _deathDate?.toIso8601String().split('T')[0],
        'generation': _generation,
        // avatar会在上传图片后设置
      };

      // 显示加载中
      Get.dialog(
        Center(child: CircularProgressIndicator()),
        barrierDismissible: false,
      );

      // 先上传头像（如果有）
      String? avatarUrl;
      if (_avatarImage != null) {
        // 这里应该调用上传图片的API，暂时跳过
        // avatarUrl = await _uploadImage(_avatarImage!);
      }

      if (avatarUrl != null) {
        requestBody['avatar'] = avatarUrl;
      }

      // 发送请求
      final response = await _httpClient.post(
        '/family/members/add',
        body: requestBody,
      );

      // 关闭加载弹窗
      Get.back();

      if (response['code'] == 200) {
        // 获取新添加的成员信息
        final newMember = response['data'];

        // 如果同时需要添加关系
        if (_selectedRelationship != null) {
          // 构建关系请求参数
          final relationshipBody = {
            // "member1": {
            //   "id": PrefsUtil.getUserId(), // 当前登录用户
            // },
            "member2": {"id": newMember['id']},
            "type": _selectedRelationship!.toUpperCase(),
            "verificationStatus": "PENDING",
            "startDate": DateTime.now().toIso8601String().split('T')[0],
          };

          // 发送添加关系请求
          final relationshipResponse = await _httpClient.post(
            '/family/relationships',
            body: relationshipBody,
          );

          if (relationshipResponse['code'] == 200) {
            Get.snackbar('success'.tr, 'member_and_relationship_added'.tr);
          } else {
            Get.snackbar('success'.tr, 'member_added_relationship_failed'.tr);
          }
        } else {
          Get.snackbar('success'.tr, 'member_added_successfully'.tr);
        }

        // 返回上一页
        Future.delayed(Duration(seconds: 2), () {
          Get.back();
        });
      } else {
        Get.snackbar(
          'error'.tr,
          response['message'] ?? 'failed_to_add_member'.tr,
        );
      }
    } catch (e) {
      Get.back(); // 关闭加载弹窗
      LogUtil.error('Add member error: $e');
      Get.snackbar('error'.tr, 'failed_to_add_member'.tr);
    }
  }

  // 显示用户搜索弹框
  // 修改 _showUserSearchDialog 方法
  Future<void> _showUserSearchDialog() async {
    // 导航到用户搜索页面并等待结果返回
    final selectedUser = await Get.toNamed(AppRoutes.USER_SEARCH);

    // 处理选择结果
    if (selectedUser != null) {
      setState(() {
        _selectedUser = selectedUser;
      });
    }
  }

  // 保存关联关系
  void _saveRelationships() {
    if (_isAddNewMemberMode) {
      _addNewMember();
    } else {
      LogUtil.info(_selectedUser.toString());
      if (_selectedUser == null) {
        Get.snackbar('error', 'please_add_at_least_one_relationship'.tr);
        return;
      }

      if (_selectedRelationship == null) {
        Get.snackbar('error', 'relationship_required'.tr);
        return;
      }

      // 补全存储逻辑
      _saveRelationshipData();
    }
  }

  // 新增：保存关系数据的异步方法
  Future<void> _saveRelationshipData() async {
    try {
      // 显示加载中
      Get.dialog(
        Center(child: CircularProgressIndicator()),
        barrierDismissible: false,
      );

      // 构建请求参数
      final requestBody = {
        // "member1": {
        //   "id": PrefsUtil.getCurrentUserId(), // 使用当前登录用户ID
        // },
        "member2": {
          "id": _selectedUser!['id'], // 从选中的用户信息中获取ID
        },
        "type": _selectedRelationship!.toUpperCase(), // 将关系类型转换为大写
        "verificationStatus": "PENDING",
        "startDate": DateTime.now().toIso8601String().split('T')[0], // 使用当前日期
      };

      // 发送POST请求到指定URL
      final response = await _httpClient.post(
        '/family/relationships',
        body: requestBody,
      );

      // 关闭加载弹窗
      Get.back();

      LogUtil.info('API response code: ${response['code']}');
      LogUtil.info('Response data: $response');

      // 处理响应结果
      if (response['code'] == 200) {
        // 请求成功
        Get.snackbar(
          'success'.tr,
          'relationships_saved'.tr,
          duration: Duration(seconds: 2), // 设置显示时间
        );
        // 延迟返回，确保用户能看到提示
        Future.delayed(Duration(seconds: 2), () {
          Get.back();
        });
      } else {
        // 请求失败
        Get.snackbar('error'.tr, response['message'] ?? 'unknown_error'.tr);
      }
    } catch (e) {
      // 关闭加载弹窗
      Get.back();
      LogUtil.error('Save relationship error: $e');
      Get.snackbar('error'.tr, 'unknown_error'.tr);
    }
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: _isAddNewMemberMode
          ? 'add_family_member'.tr
          : 'add_family_relationships'.tr,
      actions: [
        TextButton(
          onPressed: _toggleAddMode,
          child: Text(
            _isAddNewMemberMode
                ? 'add_existing_member'.tr
                : 'add_new_member'.tr,
          ),
        ),
      ],
      body: SingleChildScrollView(
        padding: const EdgeInsets.fromLTRB(16.0, 16, 16, 32),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            if (!_isAddNewMemberMode) ...[
              // 选择已有成员
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: TextEditingController(
                        text: _selectedUser != null
                            ? '${_selectedUser!['name']} (ID: ${_selectedUser!['id']})'
                            : '',
                      ),
                      decoration: InputDecoration(
                        labelText: 'selected_user'.tr,
                        border: OutlineInputBorder(),
                        enabled: false, // 不可编辑
                      ),
                    ),
                  ),
                  SizedBox(width: 8),
                  ElevatedButton(
                    onPressed: _showUserSearchDialog,
                    child: Text('select_user'.tr),
                    style: ElevatedButton.styleFrom(
                      padding: EdgeInsets.symmetric(vertical: 8),
                    ),
                  ),
                ],
              ),
            ] else ...[
              // 新增成员表单
              // 头像上传
              Center(
                child: Stack(
                  children: [
                    CircleAvatar(
                      radius: 60,
                      backgroundImage: _avatarImage != null
                          ? FileImage(_avatarImage!)
                          : null,
                      backgroundColor: Colors.grey,
                      child: _avatarImage == null
                          ? Text(
                              'click_to_change_avatar'.tr,
                              textAlign: TextAlign.center,
                            )
                          : null,
                    ),
                    Positioned(
                      bottom: 0,
                      right: 0,
                      child: CircleAvatar(
                        backgroundColor: Colors.blue,
                        child: IconButton(
                          icon: Icon(Icons.camera_alt),
                          color: Colors.white,
                          onPressed: _pickImage,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(height: 16),

              // 姓名
              TextField(
                controller: _nameController,
                decoration: InputDecoration(
                  labelText: 'name'.tr,
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 12),

              // 性别
              DropdownButtonFormField<String>(
                value: _gender,
                decoration: InputDecoration(
                  labelText: 'gender'.tr,
                  border: OutlineInputBorder(),
                ),
                items: [
                  DropdownMenuItem(value: 'MALE', child: Text('male'.tr)),
                  DropdownMenuItem(value: 'FEMALE', child: Text('female'.tr)),
                ],
                onChanged: (value) {
                  setState(() {
                    _gender = value!;
                  });
                },
              ),
              SizedBox(height: 12),

              // 出生日期
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: TextEditingController(
                        text: _birthDate != null
                            ? '${_birthDate!.year}-${_birthDate!.month.toString().padLeft(2, '0')}-${_birthDate!.day.toString().padLeft(2, '0')}'
                            : '',
                      ),
                      decoration: InputDecoration(
                        labelText: 'birth_date'.tr,
                        border: OutlineInputBorder(),
                      ),
                      readOnly: true,
                      onTap: () => _selectDate(context, true),
                    ),
                  ),
                  SizedBox(width: 8),
                  ElevatedButton(
                    onPressed: () => _selectDate(context, true),
                    child: Text('select'.tr),
                  ),
                ],
              ),
              SizedBox(height: 12),

              // 出生地
              TextField(
                controller: _birthPlaceController,
                decoration: InputDecoration(
                  labelText: 'birth_place'.tr,
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 12),

              // 职业
              TextField(
                controller: _occupationController,
                decoration: InputDecoration(
                  labelText: 'occupation'.tr,
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 12),

              // 学历
              TextField(
                controller: _educationController,
                decoration: InputDecoration(
                  labelText: 'education'.tr,
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 12),

              // 联系方式
              TextField(
                controller: _contactInfoController,
                decoration: InputDecoration(
                  labelText: 'contact_info'.tr,
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 12),

              // 已故
              SwitchListTile(
                title: Text('deceased'.tr),
                value: _deceased,
                onChanged: (value) {
                  setState(() {
                    _deceased = value;
                  });
                },
              ),
              SizedBox(height: 12),

              // 死亡日期（如果已故）
              if (_deceased) ...[
                Row(
                  children: [
                    Expanded(
                      child: TextField(
                        controller: TextEditingController(
                          text: _deathDate != null
                              ? '${_deathDate!.year}-${_deathDate!.month.toString().padLeft(2, '0')}-${_deathDate!.day.toString().padLeft(2, '0')}'
                              : '',
                        ),
                        decoration: InputDecoration(
                          labelText: 'death_date'.tr,
                          border: OutlineInputBorder(),
                        ),
                        readOnly: true,
                        onTap: () => _selectDate(context, false),
                      ),
                    ),
                    SizedBox(width: 8),
                    ElevatedButton(
                      onPressed: () => _selectDate(context, false),
                      child: Text('select'.tr),
                    ),
                  ],
                ),
                SizedBox(height: 12),
              ],

              // 世代
              TextField(
                controller: TextEditingController(text: _generation.toString()),
                decoration: InputDecoration(
                  labelText: 'generation'.tr,
                  border: OutlineInputBorder(),
                ),
                keyboardType: TextInputType.number,
                onChanged: (value) {
                  final int? gen = int.tryParse(value);
                  if (gen != null) {
                    _generation = gen;
                  }
                },
              ),
            ],

            SizedBox(height: 16),

            // 关联关系选择框（两种模式都显示）
            DropdownButtonFormField<String>(
              value: _selectedRelationship,
              decoration: InputDecoration(
                labelText: 'select_relationship'.tr,
                border: OutlineInputBorder(),
                helperText: _isAddNewMemberMode
                    ? 'relationship_with_current_user'.tr
                    : null,
              ),
              items: _relationshipOptions.map((relation) {
                return DropdownMenuItem<String>(
                  value: relation,
                  child: Text(relation.tr),
                );
              }).toList(),
              onChanged: (value) {
                setState(() {
                  _selectedRelationship = value;
                });
              },
            ),

            SizedBox(height: 32),

            // 保存按钮
            ElevatedButton(
              onPressed: _saveRelationships,
              child: Text(
                _isAddNewMemberMode ? 'add_member'.tr : 'save_relationships'.tr,
              ),
              style: ElevatedButton.styleFrom(
                padding: EdgeInsets.symmetric(vertical: 16),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
