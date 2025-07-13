import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../widgets/base_page.dart';

class ProfilePage extends StatelessWidget {
  const ProfilePage({super.key});

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
                        _buildProfileItem(Icons.male, '性别', '男'),
                        const Divider(),
                        _buildProfileItem(Icons.location_on, '位置', '北京'),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 30),
                ElevatedButton(
                  onPressed: () {},
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 40,
                      vertical: 15,
                    ),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(30),
                    ),
                  ),
                  child: const Text('编辑资料', style: TextStyle(fontSize: 16)),
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

  Widget _buildProfileItem(IconData icon, String title, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 10),
      child: Row(
        children: [
          Icon(icon, color: Theme.of(Get.context!).primaryColor),
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
