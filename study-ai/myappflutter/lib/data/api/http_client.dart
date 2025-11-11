import 'dart:convert';
import 'dart:io';
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

  // 文件上传请求
  Future<Map<String, dynamic>> uploadFile(
    String path,
    File file, {
    String fieldName = 'file',
    Map<String, String>? headers,
    Map<String, dynamic>? queryParameters,
    Map<String, dynamic>? extraFields,
  }) async {
    if (_envController.currentEnv.value == 'mock') {
      return HttpMockService.getMockResponse(path);
    }

    final uri = buildUri(path, queryParameters);
    final requestHeaders = await HttpInterceptor.interceptRequest(headers);

    // 创建multipart请求
    final request = http.MultipartRequest('POST', uri);

    // 添加请求头
    request.headers.addAll(requestHeaders);

    // 添加文件
    final multipartFile = await http.MultipartFile.fromPath(
      fieldName,
      file.path,
    );
    request.files.add(multipartFile);

    // 添加额外字段
    if (extraFields != null) {
      request.fields.addAll(
        extraFields.map((key, value) => MapEntry(key, value.toString())),
      );
    }

    _logRequest('POST', uri.toString(), requestHeaders, {
      'file': file.path,
      ...?extraFields,
    });
    final response = await _client.send(request);
    final responseBody = await response.stream.bytesToString();

    // 构建响应对象
    final httpResponse = http.Response(
      responseBody,
      response.statusCode,
      headers: response.headers,
    );

    _logResponse(httpResponse);
    return HttpInterceptor.interceptResponse(httpResponse);
  }

  // 构建请求URL
  Uri buildUri(String path, Map<String, dynamic>? queryParameters) {
    if (path.startsWith("http") || path.startsWith("https")) {
      return Uri.parse(path);
    }

    if (path.startsWith("/")) {
      path = path.substring(1);
    }
    return Uri.parse(
      '${_envController.getBaseUrl()}/$path',
    ).replace(queryParameters: queryParameters as Map<String, String>?);
  }

  // 文件下载请求
  Future<http.StreamedResponse> download(
    String path,
    String savePath, {
    Map<String, String>? headers,
    Map<String, dynamic>? queryParameters,
    void Function(int received, int total)? onReceiveProgress,
  }) async {
    if (_envController.currentEnv.value == 'mock') {
      throw Exception('Download not supported in mock mode');
    }

    final uri = buildUri(path, queryParameters);
    final requestHeaders = await HttpInterceptor.interceptRequest(headers);

    _logRequest('GET', uri.toString(), requestHeaders, null);
    
    final request = http.Request('GET', uri);
    request.headers.addAll(requestHeaders);
    
    final response = await _client.send(request);
    
    // 检查响应状态
    if (response.statusCode != 200) {
      throw Exception('Download failed with status: ${response.statusCode}');
    }

    // 创建文件并写入数据
    final file = File(savePath);
    final sink = file.openWrite();
    
    int received = 0;
    final total = response.contentLength ?? 0;
    
    await response.stream.listen(
      (List<int> chunk) {
        received += chunk.length;
        sink.add(chunk);
        
        // 调用进度回调
        if (onReceiveProgress != null) {
          onReceiveProgress(received, total);
        }
      },
      onDone: () async {
        await sink.close();
        LogUtil.info('Download completed: $savePath');
      },
      onError: (error) {
        sink.close();
        throw Exception('Download error: $error');
      },
    ).asFuture();

    return response;
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
