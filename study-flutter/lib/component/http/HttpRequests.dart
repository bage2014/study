import 'package:dio/dio.dart';
import 'package:flutter_study/component/http/CancelRequests.dart';
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

  static Future<HttpByteResult> getBytes(
      String path,
      Map<String, dynamic> parameters,
      Map<String, String> headers,
      HttpProgressCallback onProgress,
      CancelRequests cancelRequests) async {
    return _doDownloadRequest(
        path, parameters, null, headers, "get", onProgress, cancelRequests);
  }

  static Future<HttpByteResult> postBytes(
      String path,
      Map<String, dynamic> parameters,
      Map<String, String> headers,
      HttpProgressCallback onProgress,
      CancelRequests cancelRequests) async {
    return _doDownloadRequest(
        path, parameters, null, headers, "post", onProgress, cancelRequests);
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
      HttpProgressCallback onProgress,
      CancelRequests cancelRequests) async {
    HttpByteResult result = HttpByteResult();
    try {
      path = rebuildUrl(path);
      Logs.info('_doDownloadRequest path = ${path}');
      _dio.options = _buildDownloadOption(parameters, data, headers);
      Response response;
      if ("get".compareTo(method) == 0) {
        response = await _dio.get(path,
            queryParameters: parameters, cancelToken: cancelRequests.token,
            onReceiveProgress: (int sent, int total) {
          double percent = sent / total;
          print("_doDownloadRequest onReceiveProgress ${percent}%");
          onProgress?.call(sent, total);
        });
      } else {
        response =
            await _dio.post(path, queryParameters: parameters, cancelToken: cancelRequests.token,data: data);
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
        baseUrl: HttpProp.getBaseUrl(),
//        receiveTimeout: HttpProp.timeout, // 不需要指定超时时间
        headers: headers,
        contentType: HttpProp.getContentType(),
        responseType: ResponseType.bytes,
        followRedirects: false,
        validateStatus: (status) {
          return status < 500;
        });
  }

  static BaseOptions _buildOption(Map<String, dynamic> parameters, dynamic data,
      Map<String, String> headers, int timeoutMilliseconds) {
    return BaseOptions(
      baseUrl: HttpProp.getBaseUrl(),
      connectTimeout: HttpProp.getTimeout(),
      receiveTimeout: HttpProp.getTimeout(),
      headers: headers,
      contentType: HttpProp.getContentType(),
      responseType: ResponseType.plain,
    );
  }

  static String rebuildUrl(String url) {
    if (url.startsWith("/") && HttpProp.getBaseUrl().endsWith("/")) {
      url = url.substring(1);
    }
    return url.startsWith("http") ? url : HttpProp.getBaseUrl() + url;
  }
}
