import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class HomeView extends StatelessWidget {
  const HomeView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('家庭族谱'),
        centerTitle: true,
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            const DrawerHeader(
              decoration: BoxDecoration(
                color: Colors.blue,
              ),
              child: Text(
                '菜单',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 24,
                ),
              ),
            ),
            ListTile(
              title: const Text('家族树'),
              onTap: () {
                context.push('/family-tree?familyId=1');
              },
            ),
            ListTile(
              title: const Text('成员管理'),
              onTap: () {
                context.push('/members?familyId=1');
              },
            ),
            ListTile(
              title: const Text('事件管理'),
              onTap: () {
                context.push('/events?familyId=1');
              },
            ),
            ListTile(
              title: const Text('媒体管理'),
              onTap: () {
                context.push('/media?familyId=1');
              },
            ),
            ListTile(
              title: const Text('家族管理'),
              onTap: () {
                context.push('/family-management?familyId=1');
              },
            ),
            ListTile(
              title: const Text('设置'),
              onTap: () {
                context.push('/settings');
              },
            ),
          ],
        ),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text(
              '欢迎使用家庭族谱应用',
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                context.push('/family-tree?familyId=1');
              },
              child: const Text('查看家族树'),
            ),
          ],
        ),
      ),
    );
  }
}
