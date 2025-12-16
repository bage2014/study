// 在文件顶部确保已经导入了必要的包
import 'package:flutter/material.dart';
import 'package:flutter_simple_treeview/flutter_simple_treeview.dart';
import 'package:get/get.dart';
import '../../core/config/app_routes.dart';
import '../../data/models/family_response.dart';
import '../../data/api/http_client.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import '../widgets/base_page.dart';
import 'dart:io';
import 'package:image_picker/image_picker.dart';

class FamilyPage extends StatefulWidget {
  const FamilyPage({super.key});

  @override
  State<FamilyPage> createState() => _FamilyPageState();
}

class _FamilyPageState extends State<FamilyPage> {
  final HttpClient _httpClient = HttpClient(); // 创建http client实例
  FamilyData? _familyData; // 改为可空类型
  bool _isLoading = true;
  final Map<String, bool> _expandedStates = {};

  @override
  void initState() {
    super.initState();
    _loadFamilyData();
  }

  Future<void> _loadFamilyData() async {
    try {
      final response = await _httpClient.get('/family/tree/1');
      final familyResponse = FamilyResponse.fromJson(response);
      setState(() {
        _familyData = familyResponse.data; // 直接使用可空data
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      Get.snackbar('error', '加载家族数据失败: ${e.toString()}');
    }
  }

  void _navigateToEditPage() {
    // 跳转到编辑页面，这里需要根据你的路由配置来设置
    Get.toNamed(AppRoutes.FAMILY_EDIT); // 假设编辑页面的路由名为FAMILY_EDIT
  }

  @override
  Widget build(BuildContext context) {
    // 使用BasePage组件
    return BasePage(
      title: 'family_title',
      actions: [
        IconButton(
          icon: Icon(Icons.add_circle),
          onPressed: _navigateToEditPage,
        ),
      ],
      body: _isLoading
          ? const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(),
                  SizedBox(height: 20),
                  Text('loading_family_data'),
                ],
              ),
            )
          : _familyData == null
          ? Center(child: Text('no_data'))
          : Column(
              children: [
                SingleChildScrollView(
                  child: TreeView(nodes: [_buildFamilyTreeNode(_familyData!)]),
                ),
              ],
            ),
    );
  }

  TreeNode _buildFamilyTreeNode(FamilyData member) {
    final nodeKey = member.id?.toString() ?? 'unknown';
    final isExpanded = _expandedStates[nodeKey] ?? false;

    return TreeNode(
      key: Key(nodeKey),
      content: Expanded(
        child: ConstrainedBox(
          constraints: BoxConstraints(maxWidth: 300),
          child: ListTile(
            leading: CircleAvatar(
              backgroundImage: member.avatar != null
                  ? NetworkImage(member.avatar!)
                  : null,
              child: member.avatar == null || member.avatar!.isEmpty
                  ? Text(member.name?[0] ?? '?')
                  : null,
            ),
            title: GestureDetector(
              onTap: () => _showMemberOptions(member),
              child: Text(member.name ?? 'unknown_member'),
            ),
            subtitle: Text(member.relationship ?? 'unknown_relationship'),
            onTap: () => setState(() {
              _expandedStates[nodeKey] = !isExpanded;
            }),
          ),
        ),
      ),
      children: member.children != null
          ? member.children!.map(_buildFamilyTreeNode).toList()
          : [],
    );
  }

  // 实现删除关系的方法
  Future<void> _deleteRelationship(FamilyData member) async {
    // 弹出确认对话框
    bool confirmDelete =
        await Get.dialog(
          AlertDialog(
            title: Text('confirm_deletion'.tr),
            content: Text(
              'are_you_sure_you_want_to_delete_this_relationship'.tr +
                  ' ${member.name ?? ''}',
            ),
            actions: [
              TextButton(
                onPressed: () => Get.back(result: false),
                child: Text('cancel'.tr),
              ),
              TextButton(
                onPressed: () => Get.back(result: true),
                child: Text('confirm'.tr),
                style: TextButton.styleFrom(foregroundColor: Colors.red),
              ),
            ],
          ),
        ) ??
        false;

    // 如果用户确认删除
    if (confirmDelete) {
      try {
        // 显示加载中
        Get.dialog(
          Center(child: CircularProgressIndicator()),
          barrierDismissible: false,
        );

        // 发送DELETE请求
        final response = await _httpClient.post(
          '/family/relationships/delete',
          body: {'id': member.relatedId},
        );

        // 关闭加载弹窗
        Get.back();

        // 检查响应
        if (response['code'] == 200) {
          Get.snackbar('success'.tr, 'relationship_deleted_successfully'.tr);
          // 删除完成后，刷新当前关系树
          _loadFamilyData();
        } else {
          Get.snackbar(
            'error'.tr,
            response['message'] ?? 'failed_to_delete_relationship'.tr,
          );
        }
      } catch (e) {
        // 关闭加载弹窗
        Get.back();
        LogUtil.error('Delete relationship error: $e');
        Get.snackbar('error'.tr, 'failed_to_delete_relationship'.tr);
      }
    }
  }

  // 新增：编辑成员信息
  Future<void> _editMemberInfo(FamilyData member) async {
    // 获取成员详细信息
    try {
      final response = await _httpClient.get('/family/members/${member.id}');
      if (response['code'] == 200) {
        final memberDetails = response['data'];
        _showEditMemberDialog(memberDetails);
      } else {
        Get.snackbar('error'.tr, 'failed_to_get_member_info'.tr);
      }
    } catch (e) {
      LogUtil.error('Get member info error: $e');
      Get.snackbar('error'.tr, 'failed_to_get_member_info'.tr);
    }
  }

  // 新增：显示编辑成员对话框
  void _showEditMemberDialog(Map<String, dynamic> memberData) {
    final TextEditingController nameController = TextEditingController(
      text: memberData['name'],
    );
    final TextEditingController birthPlaceController = TextEditingController(
      text: memberData['birthPlace'] ?? '',
    );
    final TextEditingController occupationController = TextEditingController(
      text: memberData['occupation'] ?? '',
    );
    final TextEditingController educationController = TextEditingController(
      text: memberData['education'] ?? '',
    );
    final TextEditingController contactInfoController = TextEditingController(
      text: memberData['contactInfo'] ?? '',
    );

    String gender = memberData['gender'] ?? 'MALE';
    bool deceased = memberData['deceased'] ?? false;
    int generation = memberData['generation'] ?? 0;

    // 解析日期
    DateTime? birthDate;
    DateTime? deathDate;
    if (memberData['birthDate'] != null) {
      birthDate = DateTime.parse(memberData['birthDate']);
    }
    if (memberData['deathDate'] != null) {
      deathDate = DateTime.parse(memberData['deathDate']);
    }

    File? avatarImage;

    // 选择日期函数
    Future<void> selectDate(BuildContext context, bool isBirthDate) async {
      final DateTime? picked = await showDatePicker(
        context: context,
        initialDate: isBirthDate
            ? birthDate ?? DateTime.now()
            : deathDate ?? DateTime.now(),
        firstDate: DateTime(1900),
        lastDate: DateTime.now(),
      );

      if (picked != null) {
        if (isBirthDate) {
          birthDate = picked;
        } else {
          deathDate = picked;
        }
      }
    }

    // 选择头像函数
    Future<void> pickImage() async {
      final picker = ImagePicker();
      final pickedFile = await picker.pickImage(source: ImageSource.gallery);

      if (pickedFile != null) {
        avatarImage = File(pickedFile.path);
      }
    }

    // 保存修改函数
    Future<void> saveChanges() async {
      try {
        // 构建请求参数
        final requestBody = {
          'id': memberData['id'],
          'name': nameController.text.trim(),
          'gender': gender,
          'birthDate': birthDate?.toIso8601String().split('T')[0],
          'birthPlace': birthPlaceController.text.trim(),
          'occupation': occupationController.text.trim(),
          'education': educationController.text.trim(),
          'contactInfo': contactInfoController.text.trim(),
          'deceased': deceased,
          'deathDate': deathDate?.toIso8601String().split('T')[0],
          'generation': generation,
        };

        // 显示加载中
        Get.dialog(
          Center(child: CircularProgressIndicator()),
          barrierDismissible: false,
        );

        // 发送更新请求
        final response = await _httpClient.post(
          '/family/members/update',
          body: requestBody,
        );

        // 关闭加载弹窗
        Get.back();

        if (response['code'] == 200) {
          Get.back(); // 关闭编辑弹窗
          Get.snackbar('success'.tr, 'member_info_updated'.tr);
          _loadFamilyData(); // 刷新数据
        } else {
          Get.snackbar(
            'error'.tr,
            response['message'] ?? 'failed_to_update_member'.tr,
          );
        }
      } catch (e) {
        // 关闭加载弹窗
        Get.back();
        LogUtil.error('Update member error: $e');
        Get.snackbar('error'.tr, 'failed_to_update_member'.tr);
      }
    }

    // 显示编辑对话框
    Get.dialog(
      AlertDialog(
        title: Text('edit_member_info'.tr),
        content: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            mainAxisSize: MainAxisSize.min,
            children: [
              // 头像
              Center(
                child: CircleAvatar(
                  radius: 50,
                  backgroundImage: avatarImage != null
                      ? FileImage(avatarImage!)
                      : memberData['avatar'] != null
                      ? NetworkImage(memberData['avatar'])
                      : null,
                  backgroundColor: Colors.grey,
                  child: memberData['avatar'] == null && avatarImage == null
                      ? Text(memberData['name'][0])
                      : null,
                ),
              ),
              Center(
                child: TextButton(
                  onPressed: pickImage,
                  child: Text('change_avatar'.tr),
                ),
              ),

              // 表单字段
              TextField(
                controller: nameController,
                decoration: InputDecoration(labelText: 'name'.tr),
              ),
              SizedBox(height: 8),

              DropdownButtonFormField<String>(
                value: gender,
                decoration: InputDecoration(labelText: 'gender'.tr),
                items: [
                  DropdownMenuItem(value: 'MALE', child: Text('male'.tr)),
                  DropdownMenuItem(value: 'FEMALE', child: Text('female'.tr)),
                ],
                onChanged: (value) {
                  gender = value!;
                },
              ),
              SizedBox(height: 8),

              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: TextEditingController(
                        text: birthDate != null
                            ? '${birthDate!.year}-${birthDate!.month.toString().padLeft(2, '0')}-${birthDate!.day.toString().padLeft(2, '0')}'
                            : '',
                      ),
                      decoration: InputDecoration(labelText: 'birth_date'.tr),
                      readOnly: true,
                      onTap: () => selectDate(context, true),
                    ),
                  ),
                  IconButton(
                    icon: Icon(Icons.calendar_today),
                    onPressed: () => selectDate(context, true),
                  ),
                ],
              ),
              SizedBox(height: 8),

              TextField(
                controller: birthPlaceController,
                decoration: InputDecoration(labelText: 'birth_place'.tr),
              ),
              SizedBox(height: 8),

              TextField(
                controller: occupationController,
                decoration: InputDecoration(labelText: 'occupation'.tr),
              ),
              SizedBox(height: 8),

              TextField(
                controller: educationController,
                decoration: InputDecoration(labelText: 'education'.tr),
              ),
              SizedBox(height: 8),

              TextField(
                controller: contactInfoController,
                decoration: InputDecoration(labelText: 'contact_info'.tr),
              ),
              SizedBox(height: 8),

              Row(
                children: [
                  Text('deceased'.tr),
                  Switch(
                    value: deceased,
                    onChanged: (value) {
                      deceased = value;
                    },
                  ),
                ],
              ),

              if (deceased) ...[
                Row(
                  children: [
                    Expanded(
                      child: TextField(
                        controller: TextEditingController(
                          text: deathDate != null
                              ? '${deathDate!.year}-${deathDate!.month.toString().padLeft(2, '0')}-${deathDate!.day.toString().padLeft(2, '0')}'
                              : '',
                        ),
                        decoration: InputDecoration(labelText: 'death_date'.tr),
                        readOnly: true,
                        onTap: () => selectDate(context, false),
                      ),
                    ),
                    IconButton(
                      icon: Icon(Icons.calendar_today),
                      onPressed: () => selectDate(context, false),
                    ),
                  ],
                ),
                SizedBox(height: 8),
              ],

              TextField(
                controller: TextEditingController(text: generation.toString()),
                decoration: InputDecoration(labelText: 'generation'.tr),
                keyboardType: TextInputType.number,
                onChanged: (value) {
                  final int? gen = int.tryParse(value);
                  if (gen != null) {
                    generation = gen;
                  }
                },
              ),
            ],
          ),
        ),
        actions: [
          TextButton(onPressed: () => Get.back(), child: Text('cancel'.tr)),
          TextButton(onPressed: saveChanges, child: Text('save'.tr)),
        ],
      ),
      barrierDismissible: false,
    );
  }

  // 新增：删除成员
  Future<void> _deleteMember(FamilyData member) async {
    Get.defaultDialog(
      title: 'delete_member'.tr,
      middleText: '${'confirm_delete_member'.tr} ${member.name}?',
      confirm: ElevatedButton(
        style: ElevatedButton.styleFrom(backgroundColor: Colors.red),
        onPressed: () async {
          try {
            // 显示加载中
            Get.dialog(
              Center(child: CircularProgressIndicator()),
              barrierDismissible: false,
            );

            // 发送删除请求
            final response = await _httpClient.post(
              '/family/members/delete',
              body: {'id': member.id},
            );

            // 关闭加载弹窗
            Get.back();

            if (response['code'] == 200) {
              Get.back(); // 关闭确认弹窗
              Get.snackbar('success'.tr, 'member_deleted'.tr);
              _loadFamilyData(); // 刷新数据
            } else {
              Get.snackbar(
                'error'.tr,
                response['message'] ?? 'failed_to_delete_member'.tr,
              );
            }
          } catch (e) {
            // 关闭加载弹窗
            Get.back();
            LogUtil.error('Delete member error: $e');
            Get.snackbar('error'.tr, 'failed_to_delete_member'.tr);
          }
        },
        child: Text('delete'.tr),
      ),
      cancel: ElevatedButton(
        onPressed: () => Get.back(),
        child: Text('cancel'.tr),
      ),
    );
  }

  void _showMemberOptions(FamilyData member) {
    Get.bottomSheet(
      SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            ListTile(
              title: Text('edit_member_info'.tr),
              onTap: () {
                Get.back();
                _editMemberInfo(member);
              },
            ),
            ListTile(
              title: Text('delete_relationship'.tr),
              textColor: Colors.red,
              onTap: () {
                Get.back();
                // 调用删除关系方法，传入当前成员ID和父成员ID
                _deleteRelationship(
                  member, // 当前选中的成员为member2
                );
              },
            ),
            ListTile(
              title: Text('delete_member'.tr),
              textColor: Colors.red,
              onTap: () {
                Get.back();
                _deleteMember(member);
              },
            ),
            ListTile(
              title: Text('view_member_info'.tr),
              onTap: () {
                print('查看用户信息: ${member.name}');
                Get.toNamed(
                  AppRoutes.PROFILE,
                  arguments: {'userId': member.id},
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
