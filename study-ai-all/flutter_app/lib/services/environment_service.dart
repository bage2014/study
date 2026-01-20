import 'package:shared_preferences/shared_preferences.dart';
import '../models/environment.dart';

// 环境服务类，用于管理环境配置的存储和读取
class EnvironmentService {
  static const String _environmentKey = 'selected_environment';
  
  // 保存用户选择的环境
  static Future<void> saveEnvironment(Environment environment) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_environmentKey, environment.name);
  }
  
  // 获取用户选择的环境，如果没有选择则返回默认环境
  static Future<Environment> getSelectedEnvironment() async {
    final prefs = await SharedPreferences.getInstance();
    final environmentName = prefs.getString(_environmentKey);
    
    if (environmentName != null) {
      return Environment.allEnvironments.firstWhere(
        (env) => env.name == environmentName,
        orElse: () => Environment.web, // 默认返回Web环境
      );
    }
    
    return Environment.web; // 默认返回Web环境
  }
}
