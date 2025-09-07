import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../widgets/base_page.dart';

class FamilyEditPage extends StatefulWidget {
  const FamilyEditPage({super.key});

  @override
  State<FamilyEditPage> createState() => _FamilyEditPageState();
}

class _FamilyEditPageState extends State<FamilyEditPage> {
  final List<Map<String, dynamic>> _addedRelationships = [];
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

  // 搜索相关状态
  final TextEditingController _searchController = TextEditingController();
  final List<Map<String, dynamic>> _searchResults = [];
  int _currentPage = 1;
  int _totalPages = 1;
  bool _isSearching = false;

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  // 显示用户搜索弹框
  Future<void> _showUserSearchDialog() async {
    final dialogSearchController = TextEditingController();
    int dialogCurrentPage = 1;
    List<Map<String, dynamic>> dialogSearchResults = [];
    bool dialogIsSearching = false;

    // 使用StatefulBuilder创建可更新状态的对话框
    final selectedUser = await Get.dialog(
      StatefulBuilder(
        builder: (context, setDialogState) {
          return AlertDialog(
            title: Text('search_user'.tr),
            content: SizedBox(
              width: double.maxFinite,
              child: ConstrainedBox(
                constraints: BoxConstraints(maxHeight: 400),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    // 搜索框
                    TextFormField(
                      controller: dialogSearchController,
                      decoration: InputDecoration(
                        labelText: 'enter_phone_or_email'.tr,
                        border: OutlineInputBorder(),
                        suffixIcon: IconButton(
                          icon: Icon(Icons.search),
                          onPressed: () async {
                            final searchText = dialogSearchController.text
                                .trim();
                            if (searchText.isEmpty) return;

                            setDialogState(() {
                              dialogIsSearching = true;
                            });

                            // 模拟搜索API调用
                            await Future.delayed(Duration(milliseconds: 500));

                            // 模拟分页数据
                            final mockData = [
                              {
                                'id': 1,
                                'name': '用户111',
                                'phone': '13800138001',
                                'email': 'user1@example.com',
                              },
                              {
                                'id': 2,
                                'name': '用户112',
                                'phone': '13800138002',
                                'email': 'user2@example.com',
                              },
                              {
                                'id': 3,
                                'name': '用户113',
                                'phone': '13800138003',
                                'email': 'user3@example.com',
                              },
                              {
                                'id': 4,
                                'name': '用户114',
                                'phone': '13800138004',
                                'email': 'user4@example.com',
                              },
                              {
                                'id': 5,
                                'name': '用户1122',
                                'phone': '极速5G',
                                'email': 'user5@example.com',
                              },
                            ];

                            setDialogState(() {
                              dialogSearchResults = mockData;
                              dialogCurrentPage = 1;
                              dialogIsSearching = false;
                            });
                          },
                        ),
                      ),
                      onFieldSubmitted: (value) async {
                        if (value.isEmpty) return;

                        setDialogState(() {
                          dialogIsSearching = true;
                        });

                        await Future.delayed(Duration(milliseconds: 500));

                        final mockData = [
                          {
                            'id': 1,
                            'name': '用户112',
                            'phone': '13800138001',
                            'email': 'user1@example.com',
                          },
                          {
                            'id': 2,
                            'name': '用户113',
                            'phone': '13800138002',
                            'email': 'user2@example.com',
                          },
                        ];

                        setDialogState(() {
                          dialogSearchResults = mockData;
                          dialogCurrentPage = 1;
                          dialogIsSearching = false;
                        });
                      },
                    ),

                    SizedBox(height: 16),

                    // 搜索结果
                    if (dialogIsSearching)
                      Center(child: CircularProgressIndicator()),

                    if (dialogSearchResults.isNotEmpty)
                      Expanded(
                        child: SingleChildScrollView(
                          child: Column(
                            children: [
                              Text(
                                'search_results'.tr,
                                style: TextStyle(fontWeight: FontWeight.bold),
                              ),
                              SizedBox(height: 8),
                              ...dialogSearchResults.map((user) {
                                return ListTile(
                                  title: Text(user['name']),
                                  subtitle: Text(
                                    user['phone'] ?? user['email'] ?? '',
                                  ),
                                  onTap: () {
                                    Navigator.of(context).pop(user);
                                  },
                                );
                              }).toList(),

                              // 分页控件
                              if (_totalPages > 1)
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    IconButton(
                                      icon: Icon(Icons.arrow_back),
                                      onPressed: dialogCurrentPage > 1
                                          ? () {
                                              setDialogState(() {
                                                dialogCurrentPage--;
                                              });
                                            }
                                          : null,
                                    ),
                                    Text('$dialogCurrentPage / $_totalPages'),
                                    IconButton(
                                      icon: Icon(Icons.arrow_forward),
                                      onPressed: dialogCurrentPage < _totalPages
                                          ? () {
                                              setDialogState(() {
                                                dialogCurrentPage++;
                                              });
                                            }
                                          : null,
                                    ),
                                  ],
                                ),
                            ],
                          ),
                        ),
                      ),
                  ],
                ),
              ),
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.of(context).pop(),
                child: Text('cancel'.tr),
              ),
            ],
          );
        },
      ),
    );

    // 处理选择结果
    if (selectedUser != null) {
      setState(() {
        _addedRelationships.add({
          'user': selectedUser,
          'relationship': null, // 初始关系为空
        });
      });
    }
  }

  // 保存关联关系
  void _saveRelationships() {
    if (_addedRelationships.isEmpty) {
      Get.snackbar('error', 'please_add_at_least_one_relationship'.tr);
      return;
    }

    // 检查所有关系是否都已选择
    for (final relationship in _addedRelationships) {
      if (relationship['relationship'] == null) {
        Get.snackbar('error', 'please_select_relationship_for_all_users'.tr);
        return;
      }
    }

    // 保存关联关系逻辑
    Get.snackbar('success', 'relationships_saved'.tr);
    Get.back();
  }

  // 删除关联关系
  void _removeRelationship(int index) {
    setState(() {
      _addedRelationships.removeAt(index);
    });
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
                          : ''
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

            // 已添加的关联关系列表
            if (_addedRelationships.isNotEmpty)
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'added_relationships'.tr,
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  SizedBox(height: 16),
                  ..._addedRelationships.asMap().entries.map((entry) {
                    final index = entry.key;
                    final relationship = entry.value;
                    final user = relationship['user'];

                    return Card(
                      margin: EdgeInsets.only(bottom: 16),
                      child: Padding(
                        padding: const EdgeInsets.all(16.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            // 用户信息
                            Text(
                              '${user['name']} (ID: ${user['id']})',
                              style: TextStyle(fontWeight: FontWeight.bold),
                            ),
                            if (user['phone'] != null)
                              Text('Phone: ${user['phone']}'),
                            if (user['email'] != null)
                              Text('Email: ${user['email']}'),

                            SizedBox(height: 12),

                            // 关系选择下拉框
                            DropdownButtonFormField<String>(
                              value: relationship['relationship'],
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
                                  _addedRelationships[index]['relationship'] =
                                      value;
                                });
                              },
                              validator: (value) {
                                if (value == null) {
                                  return 'relationship_required'.tr;
                                }
                                return null;
                              },
                            ),

                            SizedBox(height: 12),

                            // 删除按钮
                            ElevatedButton(
                              onPressed: () => _removeRelationship(index),
                              child: Text('remove'.tr),
                              style: ElevatedButton.styleFrom(
                                backgroundColor: Colors.red,
                                foregroundColor: Colors.white,
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  }).toList(),
                ],
              ),

            if (_addedRelationships.isEmpty)
              Text(
                'no_relationships_added'.tr,
                style: TextStyle(color: Colors.grey),
              ),

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
