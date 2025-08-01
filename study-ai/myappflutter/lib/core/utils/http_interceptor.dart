import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:get/get.dart';
import 'package:myappflutter/core/config/app_routes.dart';
import '../constants/prefs_constants.dart';
import 'prefs_util.dart';
import 'log_util.dart';
import '../../data/api/http_client.dart';

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

    // 处理403/401错误（添加Token刷新逻辑）
    if (response.statusCode == 403 || response.statusCode == 401) {
      LogUtil.info('收到授权错误，尝试刷新Token');

      // 1. 获取当前刷新Token
      final String refreshToken =
          await PrefsUtil.getString(PrefsConstants.refreshToken) ?? '';

      if (refreshToken.isNotEmpty) {
        try {
          // 2. 调用刷新Token接口
          final HttpClient httpClient = HttpClient();
          final refreshResponse = await httpClient.post(
            '/refreshToken',
            body: {'refreshToken': refreshToken},
          );
          LogUtil.info('刷新Token响应: ${refreshResponse}');

          // 3. 处理刷新结果
          if (refreshResponse['code'] == 200 &&
              refreshResponse['data'] != null) {
            final newToken = refreshResponse['data']['token'];
            final newRefreshToken = refreshResponse['data']['refreshToken'];
            final expireTime = refreshResponse['data']['tokenExpireTime'];

            // 4. 更新本地存储的Token
            await PrefsUtil.setString(PrefsConstants.token, newToken);
            await PrefsUtil.setString(
              PrefsConstants.refreshToken,
              newRefreshToken,
            );
            await PrefsUtil.setString(
              PrefsConstants.tokenExpireTime,
              expireTime,
            );

            LogUtil.info('Token刷新成功，正在重试原始请求');
            return await _retryOriginalRequest(response);
          }
        } catch (e) {
          LogUtil.error('Token刷新请求失败: $e');
        }
      }

      // 5. 刷新失败，执行登出流程
      LogUtil.info('Token刷新失败，跳转到登录页面');
      await PrefsUtil.setString(PrefsConstants.token, '');
      await PrefsUtil.setString(PrefsConstants.refreshToken, '');
      await PrefsUtil.setString(PrefsConstants.tokenExpireTime, '');

      if (Get.currentRoute != AppRoutes.LOGIN) {
        Get.offAllNamed(AppRoutes.LOGIN);
      }
      throw Exception('用户未登录或登录已过期');
    }

    // 处理其他响应
    if (response.statusCode >= 200 && response.statusCode < 300) {
      return response.body.isEmpty ? {} : jsonDecode(response.body);
    } else {
      throw Exception('HTTP请求失败: ${response.statusCode}');
    }
  }

  // 重试原始请求
  static Future<Map<String, dynamic>> _retryOriginalRequest(
    http.Response response,
  ) async {
    // 1. 重建原始请求
    final originalRequest = http.Request(
      response.request?.method ?? 'GET',
      Uri.parse(response.request?.url.toString() ?? ''),
    );

    // 2. 添加新Token到请求头
    final newToken = await PrefsUtil.getString(PrefsConstants.token) ?? '';
    if (newToken.isNotEmpty) {
      originalRequest.headers['Authorization'] = newToken;
    }

    // 3. 复制原始请求体和其他头信息
    if (response.request?.headers != null) {
      originalRequest.headers.addAll(response.request!.headers);
    }
    // 修复：检查请求类型并安全转换
    if (response.request is http.Request) {
      final request = response.request as http.Request;
      originalRequest.bodyBytes = request.bodyBytes;
    }

    // 4. 发送重试请求
    final http.Client client = http.Client();
    final retryResponse = await client.send(originalRequest);
    final responseBody = await retryResponse.stream.bytesToString();

    return json.decode(responseBody) as Map<String, dynamic>;
  }
}
