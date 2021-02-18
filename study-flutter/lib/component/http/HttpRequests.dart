import 'package:dio/dio.dart';
import 'package:flutter_study/component/http/HttpByteResult.dart';
import 'package:flutter_study/component/http/HttpProgressCallback.dart';
import 'package:flutter_study/component/http/HttpResult.dart';
import 'package:flutter_study/component/log/Logs.dart';
import 'package:flutter_study/prop/HttpProp.dart';

class HttpRequests {
  static Dio _dio;

  static void init() {
    _dio = Dio();
  }

  static Future<HttpByteResult> getBytes(String path,
      Map<String, dynamic> parameters, Map<String, String> headers) async {
    return _doDownloadRequest(path, parameters, null, headers, "get", null);
  }

  static Future<HttpByteResult> postBytes(String path,
      Map<String, dynamic> parameters, Map<String, String> headers) async {
    return _doDownloadRequest(path, parameters, null, headers, "post", null);
  }

  static Future<HttpResult> get(String path, Map<String, dynamic> parameters,
      Map<String, String> headers) async {
    return _doBaseRequest(path, parameters, null, headers, "get", null);
  }

  static Future<HttpResult> post(String path, Map<String, dynamic> parameters,
      Map<String, String> headers) async {
    return _doBaseRequest(path, parameters, null, headers, "post", null);
  }

  static Future<HttpResult> postRaw(
      String path, dynamic data, Map<String, String> headers) async {
    return _doBaseRequest(path, null, data, headers, "post", null);
  }

  static Future<HttpResult> getWith(
      String path,
      Map<String, dynamic> parameters,
      Map<String, String> headers,
      int timeoutMilliseconds) async {
    return _doBaseRequest(
        path, parameters, null, headers, "get", timeoutMilliseconds);
  }

  static Future<HttpResult> postWith(
      String path,
      Map<String, dynamic> parameters,
      Map<String, String> headers,
      int timeoutMilliseconds) async {
    return _doBaseRequest(
        path, parameters, null, headers, "post", timeoutMilliseconds);
  }

  static Future<HttpResult> postRawWith(String path, dynamic data,
      Map<String, String> headers, int timeoutMilliseconds) async {
    return _doBaseRequest(
        path, null, data, headers, "post", timeoutMilliseconds);
  }

  static Future<HttpByteResult> _doDownloadRequest(
      String path,
      Map<String, dynamic> parameters,
      dynamic data,
      Map<String, String> headers,
      String method,
      HttpProgressCallback onReceiveProgress) async {
    HttpByteResult result = HttpByteResult();
    try {
      path = rebuildUrl(path);
      Logs.info('_doDownloadRequest path = ${path}');
      _dio.options = _buildDownloadOption(parameters, data, headers);
      Response response;
      if ("get".compareTo(method) == 0) {
        response = await _dio.get(path, queryParameters: parameters,
            onReceiveProgress: (int sent, int total) {
          double percent = sent / total;
          print("_doDownloadRequest onReceiveProgress ${percent}%");
          onReceiveProgress?.call(sent, total);
        });
      } else {
        response =
            await _dio.post(path, queryParameters: parameters, data: data);
      }
      Logs.info('_doDownloadRequest statusCode = ${response.statusCode}');
      result.responseBytes = response.data;
      result.statusCode = response.statusCode;
      result.headers = response.headers?.map;
      return result;
    } catch (e) {
      Logs.info('_doDownloadRequest error' + e.toString());
    }
    return result;
  }

  static Future<HttpResult> _doBaseRequest(
      String path,
      Map<String, dynamic> parameters,
      dynamic data,
      Map<String, String> headers,
      String method,
      int timeoutMilliseconds) async {
    HttpResult result = HttpResult();
    try {
      path = rebuildUrl(path);
      Logs.info('_doBaseRequest path = ${path}');
      _dio.options =
          _buildOption(parameters, data, headers, timeoutMilliseconds);
      Response response;
      if ("get".compareTo(method) == 0) {
        response = await _dio.get(path, queryParameters: parameters);
      } else {
        response =
            await _dio.post(path, queryParameters: parameters, data: data);
      }
      Logs.info('_doBaseRequest statusCode = ${response.statusCode}');
      result.responseBody = response.data;
      result.statusCode = response.statusCode;
      result.headers = response.headers?.map;
      return result;
    } catch (e) {
      Logs.info('_doBaseRequest error' + e.toString());
    }
    return result;
  }

  static BaseOptions _buildDownloadOption(Map<String, dynamic> parameters,
      dynamic data, Map<String, String> headers) {
    return BaseOptions(
        baseUrl: HttpProp.baseUrl,
//        receiveTimeout: HttpProp.timeout, // 不需要指定超时时间
        headers: headers,
        contentType: HttpProp.contentType,
        responseType: ResponseType.bytes,
        followRedirects: false,
        validateStatus: (status) {
          return status < 500;
        });
  }

  static BaseOptions _buildOption(Map<String, dynamic> parameters, dynamic data,
      Map<String, String> headers, int timeoutMilliseconds) {
    return BaseOptions(
      baseUrl: HttpProp.baseUrl,
      connectTimeout: HttpProp.timeout,
      receiveTimeout: HttpProp.timeout,
      headers: headers,
      contentType: HttpProp.contentType,
      responseType: ResponseType.plain,
    );
  }

  static String rebuildUrl(String url) {
    if (url.startsWith("/") && HttpProp.baseUrl.endsWith("/")) {
      url = url.substring(1);
    }
    return url.startsWith("http") ? url : HttpProp.baseUrl + url;
  }
}
