import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../widgets/base_page.dart';

class UserSearchPage extends StatefulWidget {
  const UserSearchPage({super.key});

  @override
  State<UserSearchPage> createState() => _UserSearchPageState();
}

class _UserSearchPageState extends State<UserSearchPage> {
  final TextEditingController _searchController = TextEditingController();
  int _currentPage = 0; // API使用0-based页码
  int _totalPages = 1;
  int _pageSize = 10;
  List<Map<String, dynamic>> _searchResults = [];
  bool _isSearching = false;

  // 创建HttpClient实例
  final HttpClient _httpClient = HttpClient();

  // 搜索用户方法
  Future<void> searchUsers(String keyword, int page) async {
    if (keyword.isEmpty) return;

    setState(() {
      _isSearching = true;
    });

    try {
      // 使用HttpClient组件发送POST请求
      final response = await _httpClient.post(
        'queryUsers',
        body: {'keyword': keyword, 'page': page, 'pageSize': _pageSize},
      );

      // 检查响应格式
      if (response['code'] == 200 && response['data'] != null) {
        final usersData = response['data'];

        // 格式化用户数据以适配现有UI
        final formattedUsers = usersData['users'].map<Map<String, dynamic>>((
          user,
        ) {
          return {
            'id': user['id'],
            'name': user['username'],
            'phone': user['phone'] ?? '',
            'email': user['email'],
            'avatarUrl': user['avatarUrl'],
          };
        }).toList();

        setState(() {
          _searchResults = formattedUsers;
          _currentPage = page;
          _totalPages = response['data']['totalPages'];
          _isSearching = false;
        });
      } else {
        Get.snackbar('error', response['message'] ?? 'search_failed'.tr);
        setState(() {
          _isSearching = false;
        });
      }
    } catch (e) {
      LogUtil.error('Search error: $e');
      Get.snackbar('error', 'search_failed'.tr);
      setState(() {
        _isSearching = false;
      });
    }
  }

  // 分页查询方法
  void handlePagination(int page) {
    if (page >= 0 && page < _totalPages) {
      searchUsers(_searchController.text.trim(), page);
    }
  }

  // 选择用户并返回
  void selectUser(Map<String, dynamic> user) {
    Get.back(result: user);
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'search_user'.tr,
      showAppBar: true,
      body: Padding(
        padding: const EdgeInsets.fromLTRB(16.0, 64, 16, 0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // 搜索框
            TextFormField(
              controller: _searchController,
              decoration: InputDecoration(
                labelText: 'enter_phone_or_email'.tr,
                border: OutlineInputBorder(),
                suffixIcon: IconButton(
                  icon: Icon(Icons.search),
                  onPressed: () {
                    searchUsers(_searchController.text.trim(), 0);
                  },
                ),
              ),
              onFieldSubmitted: (value) {
                searchUsers(value.trim(), 0);
              },
            ),

            SizedBox(height: 16),

            // 搜索结果
            if (_isSearching) Center(child: CircularProgressIndicator()),

            if (_searchResults.isNotEmpty)
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    children: [
                      Text(
                        'search_results'.tr,
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      SizedBox(height: 8),
                      ..._searchResults.map((user) {
                        return ListTile(
                          leading: user['avatarUrl'] != null
                              ? CircleAvatar(
                                  backgroundImage: NetworkImage(
                                    user['avatarUrl'],
                                  ),
                                )
                              : CircleAvatar(child: Text(user['name'][0])),
                          title: Text(user['name']),
                          subtitle: Text(user['phone'] ?? user['email'] ?? ''),
                          onTap: () {
                            selectUser(user);
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
                              onPressed: _currentPage > 0
                                  ? () {
                                      handlePagination(_currentPage - 1);
                                    }
                                  : null,
                            ),
                            Text(
                              '${_currentPage + 1} / $_totalPages',
                            ), // 显示1-based页码
                            IconButton(
                              icon: Icon(Icons.arrow_forward),
                              onPressed: _currentPage < _totalPages - 1
                                  ? () {
                                      handlePagination(_currentPage + 1);
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
    );
  }
}
