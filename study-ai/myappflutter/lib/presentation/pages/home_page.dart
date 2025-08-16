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
    return FutureBuilder<Map<String, dynamic>>(
      future: _loadHomeData(),
      builder: (context, snapshot) {
        // 加载中显示空的AppBar
        if (!snapshot.hasData) {
          return BasePage(
            title: '加载中...', // 修改加载时的标题
            actions: [
              IconButton(
                icon: Icon(Icons.settings),
                onPressed: () => Get.toNamed(AppRoutes.SETTINGS),
              ),
            ],
            body: LoadingWrapper<Map<String, dynamic>>(
              loader: _loadHomeData,
              builder: (data) => _buildHomeContent(),
            ),
          );
        }

        // 数据加载完成后显示带用户名和头像的AppBar
        final data = snapshot.data!;
        return BasePage(
          title: data['username'], // 将标题设置为用户名
          actions: [
            // 用户头像
            Padding(
              padding: const EdgeInsets.only(right: 8.0),
              child: CircleAvatar(
                radius: 20,
                backgroundImage: data['avatarUrl'].isNotEmpty
                    ? NetworkImage(data['avatarUrl'])
                    : const AssetImage('assets/images/user_null.png')
                          as ImageProvider,
              ),
            ),
            IconButton(
              icon: Icon(Icons.settings),
              onPressed: () => Get.toNamed(AppRoutes.SETTINGS),
            ),
          ],
          body: LoadingWrapper<Map<String, dynamic>>(
            loader: _loadHomeData,
            builder: (data) => _buildHomeContent(), // 移除username参数
          ),
        );
      },
    );
  }

  // 修改_buildHomeContent方法，移除username参数和显示
  Widget _buildHomeContent() {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: Column(
        children: [
          const SizedBox(height: 86),
          // 保留原有的GridView.count
          Expanded(
            child: GridView.count(
              crossAxisCount: 3,
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
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
                // 添加位置页面菜单卡片
                _buildMenuCard(
                  icon: Icons.location_on,
                  title: 'current_location',
                  onTap: () => Get.toNamed(AppRoutes.CURRENT_LOCATION),
                ),
                // 添加位置页面菜单卡片
                _buildMenuCard(
                  icon: Icons.location_on,
                  title: 'find_location',
                  onTap: () => Get.toNamed(AppRoutes.FIND_LOCATION),
                ),
                // 添加TV菜单卡片
                _buildMenuCard(
                  icon: Icons.tv,
                  title: 'tv_player',
                  onTap: () => Get.toNamed(AppRoutes.TV_PLAYER),
                ),
                _buildMenuCard(
                  icon: Icons.tv,
                  title: 'TV 列表',
                  onTap: () => Get.toNamed(AppRoutes.TV_LIST),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  // 保留_buildMenuCard方法
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
