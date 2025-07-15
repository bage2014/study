import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../core/config/app_routes.dart';
import '../widgets/base_page.dart'; // 使用基础页面组件

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'home',
      actions: [
        IconButton(
          icon: Icon(Icons.settings),
          onPressed: () => Get.toNamed(AppRoutes.SETTINGS),
        ),
      ],
      body: FutureBuilder<SharedPreferences>(
        future: SharedPreferences.getInstance(),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            final prefs = snapshot.data!;
            final username = prefs.getString('username') ?? '未登录';
            final avatarUrl = prefs.getString('avatarUrl');

            return _buildHomeContent(username, avatarUrl);
          }
          return Center(child: CircularProgressIndicator());
        },
      ),
    );
  }

  Widget _buildHomeContent(String username, String? avatarUrl) {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          // 用户头像和名称
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              CircleAvatar(
                radius: 24,
                backgroundImage: avatarUrl != null
                    ? NetworkImage(avatarUrl)
                    : const AssetImage('assets/images/user_null.png')
                          as ImageProvider,
              ),
              const SizedBox(width: 12),
              Text(
                username,
                style: Theme.of(Get.context!).textTheme.titleLarge,
              ),
            ],
          ),
          const SizedBox(height: 16),
          // 原有的GridView.count
          Expanded(
            child: GridView.count(
              crossAxisCount: 2,
              children: [
                _buildMenuCard(
                  icon: Icons.person,
                  title: 'profile',
                  onTap: () => Get.toNamed(AppRoutes.PROFILE),
                ),
                _buildMenuCard(
                  icon: Icons.family_restroom,
                  title: 'family',
                  onTap: () => Get.toNamed(AppRoutes.FAMILY),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildMenuCard({
    required IconData icon,
    required String title,
    required VoidCallback onTap,
  }) {
    return Card(
      elevation: 8,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(16),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, size: 48, color: Theme.of(Get.context!).primaryColor),
            const SizedBox(height: 16),
            Text(
              title.tr, // 使用翻译文本
              style: TextStyle(
                fontSize: 18,
                color: Theme.of(Get.context!).primaryColorDark,
                fontWeight: FontWeight.bold,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
