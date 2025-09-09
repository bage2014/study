import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/config/app_routes.dart';
import 'package:myappflutter/core/utils/log_util.dart';
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
    'father',
    'mother',
    'son',
    'daughter',
    'husband',
    'wife',
    'brother',
    'sister',
    'grandfather',
    'grandmother',
    'grandson',
    'granddaughter',
    'uncle',
    'aunt',
    'nephew',
    'niece',
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

    // 保存关联关系逻辑
    Get.snackbar('success', 'relationships_saved'.tr);
    Get.back();
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'add_family_relationships',
      body: Padding(
        padding: const EdgeInsets.fromLTRB(16.0, 64, 16, 0),
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
                    padding: EdgeInsets.symmetric(vertical: 16),
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
