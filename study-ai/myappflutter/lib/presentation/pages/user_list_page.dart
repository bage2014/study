import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../widgets/base_page.dart';

class UserListPage extends StatefulWidget {
  const UserListPage({Key? key}) : super(key: key);

  @override
  State<UserListPage> createState() => _UserListPageState();
}

class _UserListPageState extends State<UserListPage> {
  final TextEditingController _searchController = TextEditingController();
  int _currentPage = 0; // API使用0-based页码
  int _totalPages = 1;
  int _pageSize = 10; // 调整为与后台一致的分页大小
  List<Map<String, dynamic>> _userList = [];
  bool _isLoading = false;

  // 创建HttpClient实例
  final HttpClient _httpClient = HttpClient();

  // 初始化加载用户列表
  @override
  void initState() {
    super.initState();
    loadUsers(_currentPage);
  }

  // 加载用户列表方法
  Future<void> loadUsers(int page) async {
    setState(() {
      _isLoading = true;
    });

    try {
      // 构建查询参数
      Map<String, dynamic> params = {
        'page': page,
        'size': _pageSize,
      };

      // 如果有搜索关键词，添加到参数中
      String keyword = _searchController.text.trim();
      if (keyword.isNotEmpty) {
        params['keyword'] = keyword;
      }

      // 发送GET请求
      final response = await _httpClient.get(
        '/queryUsers',
        queryParameters: params,
      );

      // 检查响应格式
      if (response['code'] == 200 && response['data'] != null) {
        final data = response['data'];

        // 格式化用户数据
        final usersList = data['users'] ?? [];
        final formattedUsers = usersList.map<Map<String, dynamic>>((user) {
          return {
            'id': user['id'],
            'username': user['username'],
            'email': user['email'] ?? '',
            'phone': user['phone'] ?? '',
            'avatarUrl': user['avatarUrl'] ?? '',
          };
        }).toList();

        setState(() {
          _userList = formattedUsers;
          _currentPage = page;
          _totalPages = data['totalPages'] ?? 1;
          _isLoading = false;
        });
      } else {
        Get.snackbar('error', response['message'] ?? 'load_failed'.tr);
        setState(() {
          _isLoading = false;
        });
      }
    } catch (e) {
      LogUtil.error('Load users error: $e');
      Get.snackbar('error', 'load_failed'.tr);
      setState(() {
        _isLoading = false;
      });
    }
  }

  // 搜索用户方法
  void searchUsers() {
    loadUsers(0); // 搜索时从第一页开始
  }

  // 分页查询方法
  void handlePagination(int page) {
    if (page >= 0 && page < _totalPages) {
      loadUsers(page);
    }
  }

  // 添加用户方法
  void addUser() {
    // 打开添加用户的表单弹窗
    showDialog(
      context: context,
      builder: (BuildContext context) {
        // 表单控制器
        final TextEditingController usernameController = TextEditingController();
        final TextEditingController emailController = TextEditingController();
        final TextEditingController phoneController = TextEditingController();
        final TextEditingController passwordController = TextEditingController();

        return AlertDialog(
          title: Text('add_user'.tr),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextField(
                  controller: usernameController,
                  decoration: InputDecoration(labelText: 'username'.tr),
                ),
                TextField(
                  controller: emailController,
                  decoration: InputDecoration(labelText: 'email'.tr),
                  keyboardType: TextInputType.emailAddress,
                ),
                TextField(
                  controller: phoneController,
                  decoration: InputDecoration(labelText: 'phone'.tr),
                  keyboardType: TextInputType.phone,
                ),
                TextField(
                  controller: passwordController,
                  decoration: InputDecoration(labelText: 'password'.tr),
                  obscureText: true,
                ),
              ],
            ),
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: Text('cancel'.tr),
            ),
            TextButton(
              onPressed: () async {
                // 关闭弹窗
                Navigator.of(context).pop();

                // 调用添加用户API
                await _createUser({
                  'username': usernameController.text,
                  'email': emailController.text,
                  'phone': phoneController.text,
                  'password': passwordController.text,
                });
              },
              child: Text('confirm'.tr),
            ),
          ],
        );
      },
    );
  }

  // 创建用户API调用
  Future<void> _createUser(Map<String, dynamic> userData) async {
    try {
      // 显示加载中
      Get.dialog(
        Center(child: CircularProgressIndicator()),
        barrierDismissible: false,
      );

      // 调用API
      final response = await _httpClient.post(
        '/register',
        body: userData,
      );

      // 关闭加载弹窗
      Get.back();

      // 检查响应
      if (response['code'] == 200) {
        Get.snackbar('success', 'user_added_successfully'.tr);
        // 添加成功后刷新用户列表
        loadUsers(0);
      } else {
        Get.snackbar('error', response['message'] ?? 'failed_to_add_user'.tr);
      }
    } catch (e) {
      // 关闭加载弹窗
      Get.back();
      LogUtil.error('Add user error: $e');
      Get.snackbar('error', 'failed_to_add_user'.tr);
    }
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'user_list',
      showAppBar: true,
      actions: [
        // 添加用户按钮
        IconButton(
          icon: Icon(Icons.add),
          onPressed: addUser,
          tooltip: 'add_user'.tr,
        ),
      ],
      body: Padding(
        padding: const EdgeInsets.fromLTRB(16.0, 16, 16, 0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // 搜索框
            TextFormField(
              controller: _searchController,
              decoration: InputDecoration(
                labelText: 'enter_phone_email_or_username'.tr,
                border: OutlineInputBorder(),
                suffixIcon: IconButton(
                  icon: Icon(Icons.search),
                  onPressed: searchUsers,
                ),
              ),
              onFieldSubmitted: (value) {
                searchUsers();
              },
            ),

            SizedBox(height: 16),

            // 用户列表
            if (_isLoading) Center(child: CircularProgressIndicator()),

            if (!_isLoading && _userList.isNotEmpty)
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    children: [
                      Text(
                        'user_list'.tr,
                        style: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      SizedBox(height: 8),
                      ..._userList.map((user) {
                        return ListTile(
                          leading:
                              user['avatarUrl'] != null &&
                                  user['avatarUrl'].isNotEmpty
                              ? CircleAvatar(
                                  backgroundImage: NetworkImage(
                                    user['avatarUrl'],
                                  ),
                                )
                              : CircleAvatar(child: Text(user['username'][0])),
                          title: Text(user['username']),
                          subtitle: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              if (user['email'] != null && user['email'].isNotEmpty)
                                Text('Email: ${user['email']}'),
                              if (user['phone'] != null && user['phone'].isNotEmpty)
                                Text('Phone: ${user['phone']}'),
                            ],
                          ),
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
                              style: TextStyle(fontSize: 14),
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

            if (!_isLoading && _userList.isEmpty)
              Expanded(
                child: Center(
                  child: Text('no_users_found'.tr),
                ),
              ),
          ],
        ),
      ),
    );
  }
}
