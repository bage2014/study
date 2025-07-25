// 首先，我们需要导入额外的包
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:myappflutter/data/api/http_client.dart';
import '../widgets/base_page.dart';
import 'package:myappflutter/core/utils/date_util.dart';

// 将StatelessWidget改为StatefulWidget
class ProfilePage extends StatefulWidget {
  const ProfilePage({super.key});

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  // 初始化HttpClient
  final HttpClient _httpClient = HttpClient();

  // 状态变量
  String _gender = '男'; // 初始性别
  DateTime? _birthDate; // 初始出生日期
  bool _isEditing = false; // 编辑状态
  bool _isLoading = false; // 加载状态

  @override
  Widget build(BuildContext context) {
    final arguments = Get.arguments ?? {};
    final userId = arguments['userId'];
    print('用户ID: $userId');

    return BasePage(
      title: 'profile',
      body: Center(
        child: SingleChildScrollView(
          child: Center(
            child: Column(
              children: [
                const SizedBox(height: 20),
                Card(
                  elevation: 8,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(100),
                  ),
                  child: CircleAvatar(
                    radius: 60,
                    backgroundColor: Theme.of(context).primaryColor,
                    child: CircleAvatar(
                      radius: 55,
                      backgroundImage: AssetImage(
                        'assets/images/user_null.png',
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 30),
                Card(
                  elevation: 4,
                  margin: const EdgeInsets.symmetric(horizontal: 20),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(15),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(20),
                    child: Column(
                      children: [
                        _buildProfileItem(Icons.person, '姓名', '张三'),
                        const Divider(),
                        _buildGenderItem(), // 替换为可编辑的性别项
                        const Divider(),
                        _buildBirthDateItem(), // 替换为可编辑的出生日期项
                        const Divider(),
                        _buildProfileItem(Icons.location_on, '位置', '北京'),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 30),
                ElevatedButton(
                  onPressed: _isLoading ? null : _toggleEditing,
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 40,
                      vertical: 15,
                    ),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(30),
                    ),
                  ),
                  child: Text(
                    _isEditing ? '保存修改' : '编辑资料',
                    style: const TextStyle(fontSize: 16),
                  ),
                ),

                // 学校信息卡片
                _buildInfoCard(
                  title: '学校信息',
                  icon: Icons.school,
                  content: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildInfoItem('学校名称', '北京大学'),
                      _buildInfoItem('专业', '计算机科学'),
                      _buildInfoItem('入学年份', '2018'),
                    ],
                  ),
                ),

                const SizedBox(height: 20),

                // 工作信息卡片
                _buildInfoCard(
                  title: '工作信息',
                  icon: Icons.work,
                  content: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildInfoItem('公司名称', '腾讯科技'),
                      _buildInfoItem('职位', '高级工程师'),
                      _buildInfoItem('入职时间', '2022'),
                    ],
                  ),
                ),

                const SizedBox(height: 20),

                // 家庭信息卡片
                _buildInfoCard(
                  title: '家庭信息',
                  icon: Icons.family_restroom,
                  content: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildInfoItem('婚姻状况', '已婚'),
                      _buildInfoItem('子女数量', '1'),
                      _buildInfoItem('家庭住址', '北京市朝阳区'),
                    ],
                  ),
                ),

                const SizedBox(height: 30),
              ],
            ),
          ),
        ),
      ),
    );
  }

  // 构建普通的资料项
  Widget _buildProfileItem(IconData icon, String title, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10),
      child: Row(
        children: [
          Icon(icon, color: Theme.of(context).primaryColor),
          const SizedBox(width: 15),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                title,
                style: TextStyle(fontSize: 14, color: Colors.grey[600]),
              ),
              const SizedBox(height: 5),
              Text(
                value,
                style: const TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  // 构建性别选择项
  Widget _buildGenderItem() {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10),
      child: Row(
        children: [
          Icon(Icons.male, color: Theme.of(context).primaryColor),
          const SizedBox(width: 15),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                '性别',
                style: TextStyle(fontSize: 14, color: Colors.grey[600]),
              ),
              const SizedBox(height: 5),
              if (_isEditing) ...[
                Row(
                  children: [
                    Radio(
                      value: '男',
                      groupValue: _gender,
                      onChanged: (value) {
                        setState(() {
                          _gender = value!;
                        });
                      },
                    ),
                    const Text('男'),
                    Radio(
                      value: '女',
                      groupValue: _gender,
                      onChanged: (value) {
                        setState(() {
                          _gender = value!;
                        });
                      },
                    ),
                    const Text('女'),
                  ],
                ),
              ] else ...[
                Text(
                  _gender,
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ],
          ),
        ],
      ),
    );
  }

  // 构建出生日期项
  Widget _buildBirthDateItem() {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10),
      child: Row(
        children: [
          Icon(Icons.calendar_today, color: Theme.of(context).primaryColor),
          const SizedBox(width: 15),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                '出生日期',
                style: TextStyle(fontSize: 14, color: Colors.grey[600]),
              ),
              const SizedBox(height: 5),
              if (_isEditing) ...[
                ElevatedButton(
                  onPressed: () async {
                    final DateTime? picked = await showDatePicker(
                      context: context,
                      initialDate: _birthDate ?? DateTime.now(),
                      firstDate: DateTime(1900),
                      lastDate: DateTime.now(),
                    );
                    if (picked != null) {
                      setState(() {
                        _birthDate = picked;
                      });
                    }
                  },
                  child: Text(
                    _birthDate == null
                        ? '选择出生日期'
                        : '${_birthDate!.year}-${_birthDate!.month}-${_birthDate!.day}',
                  ),
                ),
              ] else ...[
                Text(
                  _birthDate == null
                      ? '未设置'
                      : '${_birthDate!.year}-${_birthDate!.month}-${_birthDate!.day}',
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ],
          ),
        ],
      ),
    );
  }

  // 切换编辑状态
  void _toggleEditing() {
    if (_isEditing) {
      // 保存修改
      _saveUserInfo();
    } else {
      // 进入编辑状态
      setState(() {
        _isEditing = true;
      });
    }
  }

  // 保存用户信息
  void _saveUserInfo() async {
    setState(() {
      _isLoading = true;
    });

    try {
      // 准备请求参数
      final Map<String, dynamic> params = {'gender': _gender};

      // 如果出生日期不为空，则添加到参数中
      if (_birthDate != null) {
        params['birthDate'] =
            '${_birthDate!.year}-${_birthDate!.month}-${_birthDate!.day}';
        params['birthDate'] = DateUtil.formatDate(params['birthDate']);
      }

      // 发送请求
      final response = await _httpClient.post('/updateUserInfo', body: params);

      if (response['code'] == 200) {
        Get.snackbar('更新成功', '用户信息更新成功');
        setState(() {
          _isEditing = false;
        });
      } else {
        Get.snackbar('更新失败', response['message'] ?? '更新失败，请重试');
      }
    } catch (e) {
      print('更新用户信息失败: $e');
      Get.snackbar('更新失败', '网络异常，请稍后重试');
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  // 其余方法保持不变
  Widget _buildInfoCard({
    required String title,
    required IconData icon,
    required Widget content,
  }) {
    return Card(
      elevation: 4,
      margin: const EdgeInsets.symmetric(horizontal: 20),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon, color: Theme.of(Get.context!).primaryColor),
                const SizedBox(width: 10),
                Text(
                  title,
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 10),
            const Divider(),
            const SizedBox(height: 10),
            content,
          ],
        ),
      ),
    );
  }

  Widget _buildInfoItem(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        children: [
          Text(
            '$label: ',
            style: TextStyle(fontSize: 16, color: Colors.grey[600]),
          ),
          Text(
            value,
            style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
          ),
        ],
      ),
    );
  }
}
