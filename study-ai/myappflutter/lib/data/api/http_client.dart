import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:get/get.dart';
import '../../features/controller/env_controller.dart'; // 添加导入
import '../mock/http_mock_service.dart';
import 'package:myappflutter/core/utils/log_util.dart';

class HttpClient {
  final http.Client _client = http.Client();
  final EnvController _envController = Get.find<EnvController>(); // 获取环境控制器

  // GET请求
  Future<Map<String, dynamic>> get(
    String path, {
    Map<String, String>? headers,
    Map<String, dynamic>? queryParameters,
  }) async {
    if (_envController.currentEnv.value == 'mock') {
      return HttpMockService.getMockResponse(path);
    }

    final uri = _buildUri(path, queryParameters);
    final requestHeaders = _buildHeaders(headers);

    _logRequest('GET', uri.toString(), requestHeaders, null);
    final response = await _client.get(uri, headers: requestHeaders);
    return _handleResponse(response);
  }

  // POST请求
  Future<Map<String, dynamic>> post(
    String path, {
    dynamic body,
    Map<String, String>? headers,
    Map<String, dynamic>? queryParameters,
  }) async {
    if (_envController.currentEnv.value == 'mock') {
      return HttpMockService.getMockResponse(path);
    }

    final uri = _buildUri(path, queryParameters);
    final requestHeaders = _buildHeaders(headers);
    final requestBody = jsonEncode(body);

    _logRequest('POST', uri.toString(), requestHeaders, body);
    final response = await _client.post(
      uri,
      headers: requestHeaders,
      body: requestBody,
    );
    return _handleResponse(response);
  }

  // 构建请求URL
  Uri _buildUri(String path, Map<String, dynamic>? queryParameters) {
    return Uri.parse(
      '${_envController.getBaseUrl()}$path',
    ).replace(queryParameters: queryParameters as Map<String, String>?);
  }

  // 构建请求头
  Map<String, String> _buildHeaders(Map<String, String>? customHeaders) {
    final headers = <String, String>{
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };

    if (customHeaders != null) {
      headers.addAll(customHeaders);
    }

    return headers;
  }

  // 处理响应
  Map<String, dynamic> _handleResponse(http.Response response) {
    // 打印响应日志
    _logResponse(response);

    if (response.statusCode >= 200 && response.statusCode < 300) {
      return response.body.isEmpty ? {} : jsonDecode(response.body);
    } else {
      throw Exception('HTTP请求失败: ${response.statusCode}');
    }
  }

  // 打印请求日志
  void _logRequest(
    String method,
    String url,
    Map<String, String> headers,
    dynamic body,
  ) {
    LogUtil.info('\n📤 [HTTP Request] ==============================');
    LogUtil.info('Method: $method');
    LogUtil.info('URL: $url');
    LogUtil.info('Headers: ${_formatJson(headers)}');
    if (body != null) LogUtil.info('Body: ${_formatJson(body)}');
    LogUtil.info('=============================================\n');
  }

  // 打印响应日志
  void _logResponse(http.Response response) {
    LogUtil.info('\n📥 [HTTP Response] ==============================');
    LogUtil.info('Status: ${response.statusCode}');
    LogUtil.info('Headers: ${_formatJson(response.headers)}');
    LogUtil.info('Body: ${_formatJson(jsonDecode(response.body))}');
    LogUtil.info('==============================================\n');
  }

  // JSON格式化
  String _formatJson(dynamic json) {
    try {
      return JsonEncoder.withIndent('  ').convert(json);
    } catch (e) {
      return json.toString();
    }
  }
}
