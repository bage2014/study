import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:get/get.dart';
import '../../features/controller/env_controller.dart';
import '../mock/http_mock_service.dart';
import 'package:myappflutter/core/utils/log_util.dart';
import 'package:myappflutter/core/utils/http_interceptor.dart';

class HttpClient {
  final http.Client _client = http.Client();
  final EnvController _envController = Get.find<EnvController>();

  // GET请求
  Future<Map<String, dynamic>> get(
    String path, {
    Map<String, String>? headers,
    Map<String, dynamic>? queryParameters,
  }) async {
    if (_envController.currentEnv.value == 'mock') {
      return HttpMockService.getMockResponse(path);
    }

    final uri = buildUri(path, queryParameters);
    final requestHeaders = await HttpInterceptor.interceptRequest(headers);

    _logRequest('GET', uri.toString(), requestHeaders, null);
    final response = await _client.get(uri, headers: requestHeaders);
    _logResponse(response);
    return HttpInterceptor.interceptResponse(response);
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

    final uri = buildUri(path, queryParameters);
    final requestHeaders = await HttpInterceptor.interceptRequest(headers);
    if (queryParameters != null && body == null) {
      requestHeaders['Content-Type'] = 'application/x-www-form-urlencoded';
    }
    final requestBody = jsonEncode(body);

    _logRequest('POST', uri.toString(), requestHeaders, body);
    final response = await _client.post(
      uri,
      headers: requestHeaders,
      body: requestBody,
    );
    _logResponse(response);
    return HttpInterceptor.interceptResponse(response);
  }

  // 构建请求URL
  Uri buildUri(String path, Map<String, dynamic>? queryParameters) {
    return Uri.parse(
      '${_envController.getBaseUrl()}$path',
    ).replace(queryParameters: queryParameters as Map<String, String>?);
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
    String body = response.body;
    LogUtil.info('\n📥 [HTTP Response] ==============================');
    LogUtil.info('Status: ${response.statusCode}');
    LogUtil.info('Headers: ${_formatJson(response.headers)}');
    LogUtil.info('Body: $body');
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
