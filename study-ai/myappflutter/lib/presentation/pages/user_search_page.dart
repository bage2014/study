import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../widgets/base_page.dart';

// 在文件顶部添加必要的导入
import 'package:image_picker/image_picker.dart';
import 'dart:io';

class UserSearchPage extends StatefulWidget {
  const UserSearchPage({Key? key}) : super(key: key);

  @override
  State<UserSearchPage> createState() => _UserSearchPageState();
}

class _UserSearchPageState extends State<UserSearchPage> {
  final TextEditingController _searchController = TextEditingController();
  int _currentPage = 0; // API使用0-based页码
  int _totalPages = 1;
  int _pageSize = 10; // 调整为与后台一致的分页大小
  List<Map<String, dynamic>> _searchResults = [];
  bool _isSearching = false;
  String avatarUrl = 'assets/images/user_null.png';
  final ImagePicker _imagePicker = ImagePicker();

  // 创建HttpClient实例
  final HttpClient _httpClient = HttpClient();

  // 搜索用户方法
  Future<void> searchUsers(String keyword, int page) async {
    setState(() {
      _isSearching = true;
    });

    try {
      // 使用HttpClient组件发送POST请求，修改URL为family/members/query
      final response = await _httpClient.post(
        '/family/members/query',
        body: {'keyword': keyword, 'page': page, 'pageSize': _pageSize},
      );

      // 检查响应格式
      if (response['code'] == 200 && response['data'] != null) {
        final data = response['data'];

        // 格式化用户数据以适配现有UI，先检查content是否为null
        final contentList = data['members'] ?? [];
        final formattedUsers = contentList.map<Map<String, dynamic>>((user) {
          return {
            'id': user['id'],
            'name': user['name'],
            'phone': user['contactInfo'] ?? '',
            'email': '', // 新数据结构中没有email字段
            'avatarUrl': user['avatar'] ?? '',
          };
        }).toList();

        setState(() {
          _searchResults = formattedUsers;
          _currentPage = page;
          _totalPages = data['totalPages'] ?? 0;
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

  // 添加成员方法
  void addFamilyMember() {
    // 打开添加成员的表单弹窗
    showDialog(
      context: context,
      builder: (BuildContext context) {
        // 表单控制器
        final TextEditingController nameController = TextEditingController();
        final TextEditingController avatarController = TextEditingController();
        final TextEditingController birthDateController =
            TextEditingController();
        final TextEditingController birthPlaceController =
            TextEditingController();
        final TextEditingController occupationController =
            TextEditingController();
        final TextEditingController educationController =
            TextEditingController();
        final TextEditingController contactInfoController =
            TextEditingController();

        // 表单状态
        String gender = 'MALE';
        bool deceased = false;
        int generation = 0;

        return AlertDialog(
          title: Text('add_family_member'.tr),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextField(
                  controller: nameController,
                  decoration: InputDecoration(labelText: 'name'.tr),
                ),
                // 将TextField替换为图片显示组件
                GestureDetector(
                  onTap: () {
                    // 显示图片选择对话框
                    _showImageSourceDialog(avatarController);
                  },
                  child: Card(
                    elevation: 8,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(100),
                    ),
                    child: CircleAvatar(
                      radius: 60,
                      backgroundColor: Theme.of(context).primaryColor,
                      child: CircleAvatar(
                        radius: 55,
                        backgroundImage:
                            avatarController.text.isNotEmpty &&
                                avatarController.text.startsWith('http')
                            ? NetworkImage(avatarUrl)
                            : AssetImage(avatarUrl)
                                  as ImageProvider,
                      ),
                    ),
                  ),
                ),
                Text('click_to_change_avatar'.tr),
                DropdownButtonFormField<String>(
                  value: gender,
                  decoration: InputDecoration(labelText: 'gender'.tr),
                  items: [
                    DropdownMenuItem(value: 'MALE', child: Text('male'.tr)),
                    DropdownMenuItem(value: 'FEMALE', child: Text('female'.tr)),
                  ],
                  onChanged: (value) {
                    gender = value!;
                  },
                ),
                TextField(
                  controller: birthDateController,
                  decoration: InputDecoration(
                    labelText: 'birth_date'.tr,
                    hintText: 'YYYY-MM-DD',
                  ),
                ),
                // TextField(
                //   controller: birthPlaceController,
                //   decoration: InputDecoration(labelText: 'birth_place'.tr),
                // ),
                // TextField(
                //   controller: occupationController,
                //   decoration: InputDecoration(labelText: 'occupation'.tr),
                // ),
                // TextField(
                //   controller: educationController,
                //   decoration: InputDecoration(labelText: 'education'.tr),
                // ),
                // TextField(
                //   controller: contactInfoController,
                //   decoration: InputDecoration(labelText: 'contact_info'.tr),
                // ),
                // Row(
                //   children: [
                //     Text('deceased'.tr),
                //     Switch(
                //       value: deceased,
                //       onChanged: (value) {
                //         deceased = value;
                //         setState(() {});
                //       },
                //     ),
                //   ],
                // ),
                // TextField(
                //   decoration: InputDecoration(labelText: 'generation'.tr),
                //   keyboardType: TextInputType.number,
                //   onChanged: (value) {
                //     try {
                //       generation = int.parse(value);
                //     } catch (e) {
                //       generation = 0;
                //     }
                //   },
                // ),
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

                // 调用添加成员API
                await _createFamilyMember({
                  'name': nameController.text,
                  'avatar': avatarController.text.isNotEmpty
                      ? avatarController.text
                      : null,
                  'gender': gender,
                  'birthDate': birthDateController.text,
                  'birthPlace': birthPlaceController.text,
                  'occupation': occupationController.text,
                  'education': educationController.text,
                  'contactInfo': contactInfoController.text,
                  'deceased': deceased,
                  'deathDate': deceased
                      ? birthDateController.text
                      : null, // 如果已故，使用生日作为死亡日期（实际应用中应该有单独的死亡日期输入）
                  'generation': generation,
                });
              },
              child: Text('confirm'.tr),
            ),
          ],
        );
      },
    );
  }

  // 创建家庭成员API调用
  Future<void> _createFamilyMember(Map<String, dynamic> memberData) async {
    try {
      // 显示加载中
      Get.dialog(
        Center(child: CircularProgressIndicator()),
        barrierDismissible: false,
      );

      // 调用API
      final response = await _httpClient.post(
        '/family/members',
        body: memberData,
      );

      // 关闭加载弹窗
      Get.back();

      // 检查响应
      if (response['code'] == 200) {
        Get.snackbar('success', 'member_added_successfully'.tr);
        // 添加成功后刷新搜索结果
        if (_searchController.text.trim().isNotEmpty) {
          searchUsers(_searchController.text.trim(), 0);
        }
      } else {
        Get.snackbar('error', response['message'] ?? 'failed_to_add_member'.tr);
      }
    } catch (e) {
      // 关闭加载弹窗
      Get.back();
      LogUtil.error('Add member error: $e');
      Get.snackbar('error', 'failed_to_add_member'.tr);
    }
  }

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'search_user'.tr,
      showAppBar: true,
      actions: [
        // 添加成员按钮
        IconButton(
          icon: Icon(Icons.add),
          onPressed: addFamilyMember,
          tooltip: 'add_member'.tr,
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
                          leading:
                              user['avatarUrl'] != null &&
                                  user['avatarUrl'].isNotEmpty
                              ? CircleAvatar(
                                  backgroundImage: NetworkImage(
                                    user['avatarUrl'],
                                  ),
                                )
                              : CircleAvatar(child: Text(user['name'][0])),
                          title: Text(user['name']),
                          subtitle: Text(user['phone'] ?? ''),
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

  // 添加图片选择和上传相关方法
  Future<void> _selectImage(TextEditingController controller) async {
    final XFile? image = await _imagePicker.pickImage(
      source: ImageSource.gallery,
      imageQuality: 80,
    );

    if (image != null) {
      // 上传图片
      await _uploadImage(File(image.path), controller);
    }
  }

  Future<void> _takePhoto(TextEditingController controller) async {
    final XFile? image = await _imagePicker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );

    if (image != null) {
      // 上传图片
      await _uploadImage(File(image.path), controller);
    }
  }

  // 修改_uploadImage方法中的URL构建部分
  Future<void> _uploadImage(
    File imageFile,
    TextEditingController controller,
  ) async {
    try {
      // 显示加载中对话框
      showDialog(
        context: context,
        barrierDismissible: false,
        builder: (BuildContext context) {
          return AlertDialog(
            content: Row(
              children: [
                CircularProgressIndicator(),
                SizedBox(width: 20),
                Text('uploading_image'.tr),
              ],
            ),
          );
        },
      );

      // 调用HttpClient的上传方法
      final response = await _httpClient.uploadFile(
        '/images/upload',
        imageFile,
      );

      Navigator.of(context).pop(); // 关闭加载对话框

      // 处理响应
      if (response['code'] == 200) {
        // 提取data字段
        final data = response['data'] ?? {};
        final fileName = data['fileName'] ?? '';

        // 构建图片URL - 修复类型错误，不需要传递额外参数
        final avatarUrl = _httpClient
            .buildUri(
              '/images/item/$fileName',
              null,
            ) // 或者传入空Map: <String, String>{}
            .toString();

        // 更新控制器的值
        setState(() {
          controller.text = avatarUrl;
        });

        Get.snackbar('success'.tr, 'image_uploaded'.tr);
      } else {
        Get.snackbar('error'.tr, 'image_upload_failed'.tr);
      }
    } catch (e) {
      Navigator.of(context).pop(); // 关闭加载对话框
      print('上传图片失败: $e');
      Get.snackbar('error'.tr, 'image_upload_failed'.tr);
    }
  }

  void _showImageSourceDialog(TextEditingController controller) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('select_image_source'.tr),
          content: SingleChildScrollView(
            child: ListBody(
              children: <Widget>[
                GestureDetector(
                  child: Text('select_from_gallery'.tr),
                  onTap: () {
                    Navigator.of(context).pop();
                    _selectImage(controller);
                  },
                ),
                const Padding(padding: EdgeInsets.all(8.0)),
                GestureDetector(
                  child: Text('take_photo'.tr),
                  onTap: () {
                    Navigator.of(context).pop();
                    _takePhoto(controller);
                  },
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
