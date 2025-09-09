import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/config/app_routes.dart';
import 'package:myappflutter/core/constants/prefs_constants.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/core/utils/prefs_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../widgets/base_page.dart';

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

  // 创建HttpClient实例
  final HttpClient _httpClient = HttpClient();

  @override
  void dispose() {
    super.dispose();
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

  // 新增：保存关系数据的异步方法
  Future<void> _saveRelationshipData() async {
    try {
      // 构建请求参数
      final requestBody = {
        "member1": {
          // "id": PrefsUtil.getString(PrefsConstants.userInfo)?.id, // 这里假设当前登录用户ID为1，实际应用中应从全局状态或本地存储获取
        },
        "member2": {
          "id": _selectedUser!['id'], // 从选中的用户信息中获取ID
        },
        "type": _selectedRelationship!.toUpperCase(), // 将关系类型转换为大写
        "verificationStatus": "PENDING",
        "startDate": DateTime.now().toIso8601String().split('T')[0], // 使用当前日期
        "endDate": DateTime.now().toIso8601String().split('T')[0], // 使用当前日期
      };

      // 发送POST请求到指定URL
      final response = await _httpClient.post(
        '/family/relationships',
        body: requestBody,
      );

      // 处理响应结果
      if (response['code'] == 200) {
        // 请求成功
        Get.snackbar('success', 'relationships_saved'.tr);
        Get.back();
      } else {
        // 请求失败
        Get.snackbar('error', response['message'] ?? 'unknown_error'.tr);
      }
    } catch (e) {
      LogUtil.error('Save relationship error: $e');
      Get.snackbar('error', 'unknown_error'.tr);
    }
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'add_family_relationships',
      body: Padding(
        padding: const EdgeInsets.fromLTRB(16.0, 16, 16, 0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // 修改：将按钮改为输入框+选择按钮
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

            SizedBox(height: 16),

            // 新增：关联关系选择框
            DropdownButtonFormField<String>(
              value: _selectedRelationship,
              decoration: InputDecoration(
                labelText: 'select_relationship'.tr,
                border: OutlineInputBorder(),
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
              validator: (value) {
                if (value == null) {
                  return 'relationship_required'.tr;
                }
                return null;
              },
            ),

            SizedBox(height: 16),

            Spacer(),

            // 保存按钮
            ElevatedButton(
              onPressed: _saveRelationships,
              child: Text('save_relationships'.tr),
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
