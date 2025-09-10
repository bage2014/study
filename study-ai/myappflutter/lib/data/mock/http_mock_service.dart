import 'dart:convert';
import 'package:flutter/services.dart';
import '../../core/utils/log_util.dart';

class HttpMockService {
  static const Map<String, String> _mockFilePaths = {
    '/login': 'mock/responses/login_mock.json',
    '/locations': 'mock/responses/locations_mock.json',
    '/family/tree': 'mock/responses/family_tree_mock.json',
    '/app/check': 'mock/responses/app_check_mock.json',
    '/message/query': 'mock/responses/message_query_mock.json',
    '/sendEmailCaptcha': 'mock/responses/send_email_captcha_mock.json',
    '/register': 'mock/responses/register_mock.json',
    '/profile': 'mock/responses/profile_mock.json',
    '/tv/search': 'mock/responses/tv_search_mock.json',
    '/queryUsers': 'mock/responses/user_search_mock.json',
    '/family/relationships': 'mock/responses/family_add_relation.json',
  };

  static Future<Map<String, dynamic>> getMockResponse(String path) async {
    try {
      final jsonString = await getMockResponseJsonString(path);
      LogUtil.info('getMockResponseJsonString jsonString = $jsonString');
      return json.decode(jsonString);
    } catch (e) {
      return {"code": 999, "msg": "mock json match failed"};
    }
  }

  static Future<String> getMockResponseJsonString(String path) async {
    try {
      // 优先尝试精确匹配
      final exactMatch = _mockFilePaths[path];
      if (exactMatch != null) {
        LogUtil.info('getMockResponseJsonString exactMatch = $exactMatch');
        return await rootBundle.loadString('lib/data/$exactMatch');
      }

      // 模糊匹配：检查路径是否包含mock路径中的关键字
      for (final entry in _mockFilePaths.entries) {
        if (path.contains(entry.key)) {
          LogUtil.info('getMockResponseJsonString fuzzyMatch = ${entry.value}');
          return await rootBundle.loadString('lib/data/${entry.value}');
        }
      }

      // 默认返回通用mock数据
      return '''
        {
          "success": true,
          "data": "This is mock data for $path"
        }
      ''';
    } catch (e) {
      return '''
        {
          "success": false,
          "error": "Failed to load mock data: ${e.toString()}"
        }
      ''';
    }
  }
}
