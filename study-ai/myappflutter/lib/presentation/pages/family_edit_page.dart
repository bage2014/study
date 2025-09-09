import 'package:flutter/material.dart';
import 'package:get/get.dart';
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
  Future<void> _showUserSearchDialog() async {
    final dialogSearchController = TextEditingController();
    int dialogCurrentPage = 0; // API使用0-based页码
    int dialogTotalPages = 1;
    int dialogPageSize = 10;
    List<Map<String, dynamic>> dialogSearchResults = [];
    bool dialogIsSearching = false;

    // 使用StatefulBuilder创建可更新状态的对话框
    final selectedUser = await Get.dialog(
      StatefulBuilder(
        builder: (context, setDialogState) {
          // 搜索用户方法
          Future<void> searchUsers(String keyword, int page) async {
            if (keyword.isEmpty) return;

            setDialogState(() {
              dialogIsSearching = true;
            });

            try {
              // 使用HttpClient组件发送POST请求
              final response = await _httpClient.post(
                'queryUsers',
                body: {
                  'keyword': keyword,
                  'page': page,
                  'pageSize': dialogPageSize,
                },
              );

              // 检查响应格式
              if (response['code'] == 200 && response['data'] != null) {
                final usersData = response['data'];

                // 格式化用户数据以适配现有UI
                final formattedUsers = usersData['users']
                    .map<Map<String, dynamic>>((user) {
                      return {
                        'id': user['id'],
                        'name': user['username'],
                        'phone': user['phone'] ?? '',
                        'email': user['email'],
                        'avatarUrl': user['avatarUrl'],
                      };
                    })
                    .toList();

                setDialogState(() {
                  dialogSearchResults = formattedUsers;
                  dialogCurrentPage = page;
                  dialogTotalPages = response['data']['totalPages'];
                  dialogIsSearching = false;
                });
              } else {
                Get.snackbar(
                  'error',
                  response['message'] ?? 'search_failed'.tr,
                );
                setDialogState(() {
                  dialogIsSearching = false;
                });
              }
            } catch (e) {
              LogUtil.error('Search error: $e');
              Get.snackbar('error', 'search_failed'.tr);
              setDialogState(() {
                dialogIsSearching = false;
              });
            }
          }

          // 分页查询方法
          void handlePagination(int page) {
            if (page >= 0 && page < dialogTotalPages) {
              searchUsers(dialogSearchController.text.trim(), page);
            }
          }

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
                          onPressed: () {
                            searchUsers(dialogSearchController.text.trim(), 0);
                          },
                        ),
                      ),
                      onFieldSubmitted: (value) {
                        searchUsers(value.trim(), 0);
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
                                  leading: user['avatarUrl'] != null
                                      ? CircleAvatar(
                                          backgroundImage: NetworkImage(
                                            user['avatarUrl'],
                                          ),
                                        )
                                      : CircleAvatar(
                                          child: Text(user['name'][0]),
                                        ),
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
                              if (dialogTotalPages > 1)
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    IconButton(
                                      icon: Icon(Icons.arrow_back),
                                      onPressed: dialogCurrentPage > 0
                                          ? () {
                                              handlePagination(
                                                dialogCurrentPage - 1,
                                              );
                                            }
                                          : null,
                                    ),
                                    Text(
                                      '${dialogCurrentPage + 1} / $dialogTotalPages',
                                    ), // 显示1-based页码
                                    IconButton(
                                      icon: Icon(Icons.arrow_forward),
                                      onPressed:
                                          dialogCurrentPage <
                                              dialogTotalPages - 1
                                          ? () {
                                              handlePagination(
                                                dialogCurrentPage + 1,
                                              );
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
