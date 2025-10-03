import 'package:flutter/material.dart';
import 'package:myappflutter/presentation/widgets/base_page.dart';
import 'package:myappflutter/presentation/pages/live_group_page.dart';
import 'package:myappflutter/presentation/pages/live_all_page.dart';

class LivePage extends StatefulWidget {
  const LivePage({Key? key}) : super(key: key);

  @override
  State<LivePage> createState() => _LivePageState();
}

class _LivePageState extends State<LivePage> {
  @override
  Widget build(BuildContext context) {
    return BasePage(
      title: '直播频道',
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // 分组菜单按钮
              _buildMenuButton(
                context,
                title: '频道分组',
                icon: Icons.category,
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const LiveGroupPage()),
                  );
                },
              ),
              const SizedBox(height: 24),
              // 喜欢和所有菜单按钮
              _buildMenuButton(
                context,
                title: '我的收藏与频道列表',
                icon: Icons.star,
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const LiveAllPage()),
                  );
                },
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildMenuButton(
    BuildContext context,
    {
    required String title,
    required IconData icon,
    required VoidCallback onTap,
    }
  ) {
    return ElevatedButton(
      style: ElevatedButton.styleFrom(
        padding: const EdgeInsets.symmetric(vertical: 20),
        textStyle: const TextStyle(fontSize: 18),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
        ),
      ),
      onPressed: onTap,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icon, size: 24),
          const SizedBox(width: 12),
          Text(title),
        ],
      ),
    );
  }
}
