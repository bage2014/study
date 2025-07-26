import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:get/get.dart';
import '../constants/prefs_constants.dart';
import 'prefs_util.dart';
import 'log_util.dart';

class HttpInterceptor {
  // 拦截请求，添加token
  static Future<Map<String, String>> interceptRequest(
    Map<String, String>? headers,
  ) async {
    final requestHeaders = <String, String>{
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };

    // 添加自定义头
    if (headers != null) {
      requestHeaders.addAll(headers);
    }

    // 尝试获取token并添加到请求头
    final token = await PrefsUtil.getString(PrefsConstants.token);
    if (token != null) {
      requestHeaders['Authorization'] = '$token';
      // LogUtil.info('添加token到请求头: $token');
    }

    return requestHeaders;
  }

  // 拦截响应，处理403错误
  static Future<Map<String, dynamic>> interceptResponse(
    http.Response response,
  ) async {
    // LogUtil.info('statusCode: ${response.statusCode}');
    // LogUtil.info('body  : ${response.body}');

    // 处理403错误
    if (response.statusCode == 403) {
      LogUtil.info('收到403错误，跳转到登录页面');
      // 清除登录信息
      await PrefsUtil.setString(PrefsConstants.token, '');
      await PrefsUtil.setString(PrefsConstants.tokenExpireTime, '');
      // 跳转到登录页面
      Get.offAllNamed('/login');
      throw Exception('用户未登录或登录已过期');
    }

    // 处理其他响应
    if (response.statusCode >= 200 && response.statusCode < 300) {
      return response.body.isEmpty ? {} : jsonDecode(response.body);
    } else {
      throw Exception('HTTP请求失败: ${response.statusCode}');
    }
  }
}
