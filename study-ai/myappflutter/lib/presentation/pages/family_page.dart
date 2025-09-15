// 在文件顶部确保已经导入了必要的包
import 'package:flutter/material.dart';
import 'package:flutter_simple_treeview/flutter_simple_treeview.dart';
import 'package:get/get.dart';
import '../../core/config/app_routes.dart';
import '../../data/models/family_response.dart';
import '../../data/api/http_client.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import '../widgets/base_page.dart';

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

  void _showMemberOptions(FamilyData member) {
    Get.bottomSheet(
      SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
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
