import 'package:flutter/material.dart';
import 'package:myappflutter/pages/current_location_page.dart';
import 'package:myappflutter/pages/history_location_page.dart';
import 'package:myappflutter/pages/track_location_page.dart';
import 'package:myappflutter/pages/find_location_page.dart'; // 新增导入

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '引导',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: const MainMenuPage(),
    );
  }
}

// 主菜单页面
class MainMenuPage extends StatelessWidget {
  const MainMenuPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // 渐变背景
      body: Container(
        decoration: const BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [Color(0xFFE3F2FD), Color(0xFFBBDEFB)],
          ),
        ),
        child: Column(
          children: [
            // 应用标题区域
            const Padding(
              padding: EdgeInsets.only(top: 80, bottom: 40),
              child: Column(
                children: [
                  Icon(Icons.location_on, size: 60, color: Colors.blueAccent),
                  SizedBox(height: 16),
                  Text(
                    '位置追踪应用',
                    style: TextStyle(
                      fontSize: 28,
                      fontWeight: FontWeight.bold,
                      color: Color(0xFF1A237E),
                    ),
                  ),
                ],
              ),
            ),

            // 菜单按钮区域
            Expanded(
              child: Center(
                child: SingleChildScrollView(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      // 当前位置按钮
                      _buildMenuButton(
                        context,
                        label: '当前位置',
                        icon: Icons.my_location,
                        color: const Color(0xFF1976D2),
                        onPressed: () => Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => const CurrentLocationPage()),
                        ),
                      ),
                      const SizedBox(height: 20),

                      // 历史位置按钮
                      _buildMenuButton(
                        context,
                        label: '历史位置',
                        icon: Icons.history,
                        color: const Color(0xFF3949AB),
                        onPressed: () => Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => const HistoryLocationPage()),
                        ),
                      ),
                      const SizedBox(height: 20),

                      // 位置轨迹按钮
                      _buildMenuButton(
                        context,
                        label: '位置轨迹',
                        icon: Icons.track_changes,
                        color: const Color(0xFF303F9F),
                        onPressed: () => Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => const TrackLocationPage()),
                        ),
                      ),
                      const SizedBox(height: 20),
                      // 新增: 查找位置按钮
                      _buildMenuButton(
                        context,
                        label: '查找位置',
                        icon: Icons.search,
                        color: const Color(0xFF1565C0),
                        onPressed: () => Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => const FindLocationPage()),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  // 美化的菜单按钮组件
  Widget _buildMenuButton(
    BuildContext context,
    {required String label, required IconData icon, required Color color, required VoidCallback onPressed}
  ) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 24),
      child: SizedBox(
        width: double.infinity,
        height: 80,
        child: ElevatedButton.icon(
          icon: Icon(icon, size: 32, color: Colors.white),
          label: Text(
            label,
            style: const TextStyle(fontSize: 20, color: Colors.white, fontWeight: FontWeight.w500),
          ),
          style: ElevatedButton.styleFrom(
            backgroundColor: color,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(16),
            ),
            elevation: 8,
            shadowColor: color.withOpacity(0.3),
            padding: const EdgeInsets.symmetric(horizontal: 24),
            // 添加按钮点击动画
            animationDuration: const Duration(milliseconds: 300),
          ),
          onPressed: onPressed,
        ),
      ),
    );
  }
}
