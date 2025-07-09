import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../config/app_routes.dart';
import '../common/widgets/base_page.dart'; // 使用基础页面组件

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: 'home', // 使用翻译文本
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: GridView.count(
          crossAxisCount: 2, // 保持2列布局
          crossAxisSpacing: 16,
          mainAxisSpacing: 16,
          children: [
            _buildMenuCard(
              icon: Icons.my_location,
              title: 'current_location',
              onTap: () => Get.toNamed(AppRoutes.CURRENT_LOCATION),
            ),
            _buildMenuCard(
              icon: Icons.search,
              title: 'find_location',
              onTap: () => Get.toNamed(AppRoutes.FIND_LOCATION),
            ),
            _buildMenuCard(
              icon: Icons.history,
              title: 'history_location',
              onTap: () => Get.toNamed(AppRoutes.HISTORY_LOCATION),
            ),
            _buildMenuCard(
              icon: Icons.track_changes,
              title: 'track_location',
              onTap: () => Get.toNamed(AppRoutes.TRACK_LOCATION),
            ),
            // 添加设置按钮
            _buildMenuCard(
              icon: Icons.settings,
              title: 'settings',
              onTap: () => Get.toNamed(AppRoutes.SETTINGS),
            ),
          ],
        ),
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
