import 'package:flutter/material.dart';
import 'package:flutter_simple_treeview/flutter_simple_treeview.dart';
import '../widgets/base_page.dart';
import 'package:get/get.dart';
import '../../core/config/app_routes.dart';
import '../../data/models/family_response.dart';
import '../../data/models/family_data.dart';

class FamilyPage extends StatefulWidget {
  const FamilyPage({super.key});

  @override
  State<FamilyPage> createState() => _FamilyPageState();
}

class _FamilyPageState extends State<FamilyPage> {
  final Map<String, bool> _expandedStates = {};

  @override
  Widget build(BuildContext context) {
    FamilyData familyData = FamilyResponse.fromJson(familyDataMap).data;
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

  TreeNode _buildFamilyTreeNode(FamilyData member) {
    final nodeKey = member.id.toString();
    final isExpanded = _expandedStates[nodeKey] ?? false;

    return TreeNode(
      key: Key(nodeKey),
      content: Expanded(
        child: ConstrainedBox(
          constraints: BoxConstraints(maxWidth: 300),
          child: ListTile(
            leading: CircleAvatar(
              backgroundImage: NetworkImage(member.avatar),
              child: member.avatar.isEmpty ? Text(member.name[0]) : null,
            ),
            title: GestureDetector(
              onTap: () => _showMemberOptions(member),
              child: Text(member.name),
            ),
            subtitle: Text(member.relationship),
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
              print('切换到用户: ${member.name}');
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
