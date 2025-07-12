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
          crossAxisCount: 2,
          children: [
            _buildMenuCard(
              icon: Icons.person,
              title: 'settings',
              onTap: () => Get.toNamed(AppRoutes.SETTINGS),
            ),
            _buildMenuCard(
              icon: Icons.family_restroom, 
              title: 'family', 
              onTap:  () => Get.toNamed(AppRoutes.FAMILY  ))
            
            // 其他菜单项也改为使用_buildMenuCard
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
