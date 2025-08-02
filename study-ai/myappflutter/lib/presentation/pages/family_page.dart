import 'package:flutter/material.dart';
import 'package:flutter_simple_treeview/flutter_simple_treeview.dart';
import 'package:get/get.dart';
import '../../core/config/app_routes.dart';
import '../../data/models/family_response.dart';
import '../../data/api/http_client.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import '../widgets/base_page.dart'; // 导入base_page组件

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

  @override
  Widget build(BuildContext context) {
    // 使用BasePage组件
    return BasePage(
      title: 'family_title',
      body: _isLoading
          ? const Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  CircularProgressIndicator(),
                  SizedBox(height: 20),
                  Text('loading_family_data'), // 增加加载提示文本
                ],
              ),
            )
          : _familyData == null
          ? Center(child: Text('no_data'))
          : Column(
              children: [
                SizedBox(height: 64), // 在这里添加SizedBox
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
}

void _showMemberOptions(FamilyData member) {
  Get.bottomSheet(
    SafeArea(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          ListTile(
            title: Text('switch_to_user'),
            onTap: () {
              LogUtil.info('切换到用户: \${member.name}');
              LogUtil.info('查看用户信息: \${member.name}');
              Get.back();
            },
          ),
          ListTile(
            title: Text('view_member_info'),
            onTap: () {
              print('查看用户信息: ${member.name}');
              Get.toNamed(AppRoutes.PROFILE, arguments: {'userId': member.id});
            },
          ),
        ],
      ),
    ),
  );
}
