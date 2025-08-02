import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../core/config/app_routes.dart';
import '../widgets/base_page.dart';
import '../widgets/loading_wrapper.dart';
// 新增必要导入
import 'package:myappflutter/core/utils/prefs_util.dart';
import 'package:myappflutter/core/constants/prefs_constants.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/data/api/http_client.dart';

class HomePage extends StatelessWidget {
  HomePage({super.key});

  // 新增HttpClient实例
  final HttpClient _httpClient = HttpClient();

  Future<Map<String, dynamic>> _loadHomeData() async {
    try {
      // 获取token并构建请求参数
      final String token =
          (await PrefsUtil.getString(PrefsConstants.token)) ?? '';
      final Map<String, String> queryParameters = {
        'Authorization': token.toString(),
      };

      // 调用后端API获取用户信息
      final response = await _httpClient.get(
        '/profile', // 与ProfilePage共用用户信息接口
        queryParameters: queryParameters,
      );

      // 处理后端响应数据
      if (response['code'] == 200 && response['data'] != null) {
        final data = response['data'];
        String username = data['username'] ?? '未登录';
        String avatarUrl = '';

        // 构建完整头像URL（包含认证参数）
        if (data['avatarUrl'] != null && data['avatarUrl'].isNotEmpty) {
          avatarUrl = _httpClient
              .buildUri(data['avatarUrl'], queryParameters)
              .toString();
          LogUtil.info('HomePage头像URL: $avatarUrl');
        }

        return {'username': username, 'avatarUrl': avatarUrl};
      } else {
        Get.snackbar('错误', '获取用户信息失败: ${response['message'] ?? '未知错误'}');
        return {'username': '未登录', 'avatarUrl': ''};
      }
    } catch (e) {
      LogUtil.error('获取用户信息异常: $e');
      Get.snackbar('错误', '获取用户信息失败，请稍后重试');
      return {'username': '未登录', 'avatarUrl': ''};
    }
  }

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
      body: LoadingWrapper<Map<String, dynamic>>(
        loader: _loadHomeData,
        builder: (data) =>
            _buildHomeContent(data['username'], data['avatarUrl']),
      ),
    );
  }

  Widget _buildHomeContent(String username, String? avatarUrl) {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          const SizedBox(height: 86),
          // 用户头像和名称
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              CircleAvatar(
                radius: 24,
                backgroundImage: avatarUrl != null && avatarUrl.isNotEmpty
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
                _buildMenuCard(
                  icon: Icons.message,
                  title: 'message',
                  onTap: () => Get.toNamed(AppRoutes.MESSAGE),
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
