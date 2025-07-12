import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../config/app_routes.dart';
import '../common/widgets/base_page.dart'; // 使用基础页面组件
import '../models/family_member.dart';

class FamilyPage extends StatelessWidget {
  final String jsonDate = '''
    {
  "id": "123",
  "name": "张三",
  "avatar": "https://example.com/avatars/123.jpg",
  "generation": 0,
  "relationship": "self",
  "spouses": [
    {
      "id": "124",
      "name": "李四",
      "relationship": "spouse",
      "generation": 0,
      "spouses": [],
      "parents": []
    }
  ],
  "parents": [
    {
      "id": "111",
      "name": "张父",
      "relationship": "parent",
      "generation": -1,
      "spouses": [
        {
          "id": "112",
          "name": "张母",
          "relationship": "spouse",
          "generation": -1
        }
      ],
      "parents": [
        {
          "id": "101",
          "name": "张祖父",
          "relationship": "parent",
          "generation": -2
        }
      ]
    }
  ],
  "children": [
    {
      "id": "125",
      "name": "张小三",
      "relationship": "child",
      "generation": 1,
      "spouses": [],
      "children": []
    }
  ]
}
  ''';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('家族图谱')),
      body: SingleChildScrollView(
        child: _FamilyTree(
          member: FamilyMember.fromJson(json.decode(jsonDate)),
          isRoot: true,
        ),
      ),
    );
  }
}

class _FamilyTree extends StatefulWidget {
  final FamilyMember member;
  final bool isRoot;

  const _FamilyTree({required this.member, this.isRoot = false});

  @override
  _FamilyTreeState createState() => _FamilyTreeState();
}

class _FamilyTreeState extends State<_FamilyTree> {
  bool _expanded = true;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        // 成员卡片
        GestureDetector(
          onTap: () => setState(() => _expanded = !_expanded),
          child: Card(
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Row(
                children: [
                  Icon(_expanded ? Icons.expand_less : Icons.expand_more),
                  const SizedBox(width: 8),
                  CircleAvatar(
                    backgroundImage: NetworkImage(widget.member.avatar ?? ''),
                  ),
                  const SizedBox(width: 12),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        widget.member.name ?? '',
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      Text(widget.member.relationship ?? ''),
                    ],
                  ),
                ],
              ),
            ),
          ),
        ),
        // 关系展示
        if (_expanded)
          Padding(
            padding: const EdgeInsets.only(left: 24.0),
            child: Column(
              children: [
                // 配偶
                if (widget.member.spouses?.isNotEmpty ?? false)
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        '配偶:',
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      ...widget.member.spouses!
                          .map((spouse) => _FamilyTree(member: spouse))
                          .toList(),
                    ],
                  ),
                // 父母
                if (widget.member.parents?.isNotEmpty ?? false)
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        '父母:',
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      ...widget.member.parents!
                          .map((parent) => _FamilyTree(member: parent))
                          .toList(),
                    ],
                  ),
                // 子女
                if (widget.member.children?.isNotEmpty ?? false)
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        '子女:',
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      ...widget.member.children!
                          .map((child) => _FamilyTree(member: child))
                          .toList(),
                    ],
                  ),
              ],
            ),
          ),
      ],
    );
  }
}
