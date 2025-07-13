import 'package:flutter/material.dart';
import 'package:flutter_simple_treeview/flutter_simple_treeview.dart';

class FamilyPage extends StatefulWidget {
  const FamilyPage({super.key});

  @override
  State<FamilyPage> createState() => _FamilyPageState();
}

class _FamilyPageState extends State<FamilyPage> {
  final Map<String, bool> _expandedStates = {};

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
              title: Text(memberMap['name']),
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

  @override
  Widget build(BuildContext context) {
    final familyData = {
      "id": "123",
      "name": "张三",
      "avatar":
          "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
      "generation": 0,
      "relationship": "self",
      "spouses": [
        {
          "id": "124",
          "name": "李四",
          "avatar":
              "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
          "relationship": "spouse",
          "generation": 0,
          "spouses": [],
          "parents": [],
        },
      ],
      "parents": [
        {
          "id": "111",
          "name": "张父",
          "avatar":
              "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
          "relationship": "parent",
          "generation": -1,
          "spouses": [
            {
              "id": "112",
              "name": "张母",
              "avatar":
                  "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
              "relationship": "spouse",
              "generation": -1,
            },
          ],
          "parents": [
            {
              "id": "101",
              "name": "张祖父",
              "avatar":
                  "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
              "relationship": "parent",
              "generation": -2,
            },
          ],
        },
      ],
      "children": [
        {
          "id": "125",
          "name": "张小三",
          "avatar":
              "https://avatars.githubusercontent.com/u/18094768?s=400&amp;u=1a2cacb3972a01fc3592f3c314b6e6b8e41d59b4&amp;v=4",
          "relationship": "child",
          "generation": 1,
          "spouses": [],
          "children": [],
        },
      ],
    };

    return Scaffold(
      appBar: AppBar(title: const Text('家族树')),
      body: SingleChildScrollView(
        child: TreeView(nodes: [_buildFamilyTreeNode(familyData)]),
      ),
    );
  }
}
