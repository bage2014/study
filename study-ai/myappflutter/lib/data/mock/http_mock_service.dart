import 'dart:convert';
import 'package:flutter/services.dart';

class HttpMockService {
  static const Map<String, String> _mockFilePaths = {
    '/api/login': 'mock/responses/login_mock.json',
    '/api/locations': 'mock/responses/locations_mock.json',
    '/family/tree/1': 'mock/responses/family_tree_mock.json',
  };

  static Future<Map<String, dynamic>> getMockResponse(String path) async {
    try {
      final jsonString = await getMockResponseJsonString(path);
      return json.decode(jsonString);
    } catch (e) {
      return {"code": 999, "msg": "mock json match failed"};
    }
  }

  static Future<String> getMockResponseJsonString(String path) async {
    try {
      final filePath = _mockFilePaths[path];
      if (filePath != null) {
        return await rootBundle.loadString('lib/data/$filePath');
      }
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
