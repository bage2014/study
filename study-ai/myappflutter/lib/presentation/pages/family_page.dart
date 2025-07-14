import 'package:flutter/material.dart';
import 'package:flutter_simple_treeview/flutter_simple_treeview.dart';
import 'package:get/get.dart';
import '../../core/config/app_routes.dart';
import '../../data/models/family_response.dart';
import '../../data/api/http_client.dart';
import 'package:myappflutter/core/utils/log_util.dart';

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
      Get.snackbar('错误', '加载家族数据失败: ${e.toString()}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('家族树')),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _familyData == null
          ? const Center(child: Text('暂无数据'))
          : SingleChildScrollView(
              child: TreeView(nodes: [_buildFamilyTreeNode(_familyData!)]),
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
              child: Text(member.name ?? '未知成员'),
            ),
            subtitle: Text(member.relationship ?? '未知关系'),
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
            title: Text('切换到当前用户'),
            onTap: () {
              LogUtil.info('切换到用户: \${member.name}');
              LogUtil.info('查看用户信息: \${member.name}');
              Get.back();
            },
          ),
          ListTile(
            title: Text('查看该用户信息'),
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
