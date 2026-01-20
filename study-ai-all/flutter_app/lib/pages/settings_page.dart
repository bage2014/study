import 'package:flutter/material.dart';
import 'package:flutter_app/models/environment.dart';
import 'package:flutter_app/services/environment_service.dart';
import 'package:flutter_app/api/api_service.dart';

class SettingsPage extends StatefulWidget {
  const SettingsPage({Key? key}) : super(key: key);

  @override
  State<SettingsPage> createState() => _SettingsPageState();
}

class _SettingsPageState extends State<SettingsPage> {
  late Future<Environment> _selectedEnvironment;

  @override
  void initState() {
    super.initState();
    _selectedEnvironment = EnvironmentService.getSelectedEnvironment();
  }

  // 更新选择的环境
  Future<void> _updateEnvironment(Environment environment) async {
    await EnvironmentService.saveEnvironment(environment);
    await ApiService.initialize();
    
    setState(() {
      _selectedEnvironment = Future.value(environment);
    });
    
    // 显示更新成功的提示
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('环境已更新为：${environment.name}'),
        duration: const Duration(seconds: 2),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('设置'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              '选择环境',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 20),
            FutureBuilder(
              future: _selectedEnvironment,
              builder: (context, AsyncSnapshot<Environment> snapshot) {
                if (snapshot.hasData) {
                  return ListView.builder(
                    shrinkWrap: true,
                    itemCount: Environment.allEnvironments.length,
                    itemBuilder: (context, index) {
                      final environment = Environment.allEnvironments[index];
                      final isSelected = environment.name == snapshot.data?.name;
                      
                      return Card(
                        elevation: 2,
                        child: ListTile(
                          title: Text(environment.name),
                          subtitle: Text(environment.description),
                          trailing: isSelected
                              ? const Icon(Icons.check, color: Colors.blue)
                              : null,
                          selected: isSelected,
                          onTap: () => _updateEnvironment(environment),
                        ),
                      );
                    },
                  );
                } else if (snapshot.hasError) {
                  return Text('加载环境失败: ${snapshot.error}');
                } else {
                  return const CircularProgressIndicator();
                }
              },
            ),
          ],
        ),
      ),
    );
  }
}
