import 'package:flutter/material.dart';
import 'package:flutter_simple_treeview/flutter_simple_treeview.dart';
import '../common/widgets/base_page.dart';
import '../models/family_data.dart';
import 'package:get/get.dart';
import '../config/app_routes.dart';

class FamilyPage extends StatefulWidget {
  const FamilyPage({super.key});

  @override
  State<FamilyPage> createState() => _FamilyPageState();
}

class _FamilyPageState extends State<FamilyPage> {
  final Map<String, bool> _expandedStates = {};

  @override
  Widget build(BuildContext context) {
    return BasePage(
      // 直接使用导入的familyData
      title: '家族树',
      body: Padding(
        padding: EdgeInsets.only(
          top: kToolbarHeight,
        ), // 使用kToolbarHeight常量确保与AppBar高度一致
        child: TreeView(nodes: [_buildFamilyTreeNode(familyData)]),
      ),
    );
  }

  TreeNode _buildFamilyTreeNode(dynamic member) {
    try {
      final memberMap = member as Map<String, dynamic>;
      final nodeKey = memberMap['id'] as String;
      final isExpanded = _expandedStates[nodeKey] ?? false;

      return TreeNode(
        key: Key(nodeKey),
        content: Expanded(
          child: ConstrainedBox(
            constraints: BoxConstraints(maxWidth: 300), // 添加最大宽度约束
            child: ListTile(
              leading: CircleAvatar(
                backgroundImage: NetworkImage(memberMap['avatar'] ?? ''),
                child: memberMap['avatar'] == null
                    ? Text(memberMap['name'][0])
                    : null,
              ),
              title: GestureDetector(
                onTap: () => _showMemberOptions(memberMap),
                child: Text(memberMap['name']),
              ),
              subtitle: Text(memberMap['relationship']),
              onTap: () => setState(() {
                _expandedStates[nodeKey] = !isExpanded;
              }),
            ),
          ),
        ),
        children: [
          if (memberMap['spouses'] != null)
            ...(memberMap['spouses'] as List).map(_buildFamilyTreeNode),
          if (memberMap['parents'] != null)
            ...(memberMap['parents'] as List).map(_buildFamilyTreeNode),
          if (memberMap['children'] != null)
            ...(memberMap['children'] as List).map(_buildFamilyTreeNode),
        ],
      );
    } catch (e) {
      return TreeNode(content: Text('Error loading node'));
    }
  }
}

void _showMemberOptions(Map<String, dynamic> member) {
  Get.bottomSheet(
    SafeArea(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          ListTile(
            title: Text('切换到当前用户'),
            onTap: () {
              print('切换到用户: ${member['name']}');
              Get.back();
            },
          ),
          ListTile(
            title: Text('查看该用户信息'),
            onTap: () {
              print('查看用户信息: ${member['name']}');
              Get.toNamed(
                AppRoutes.PROFILE,
                arguments: {'userId': member['id']},
              );
            },
          ),
        ],
      ),
    ),
  );
}
