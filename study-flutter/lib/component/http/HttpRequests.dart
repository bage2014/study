import 'package:dio/dio.dart';
import 'package:flutter_study/component/http/HttpResult.dart';
import 'package:flutter_study/component/log/Logs.dart';
import 'package:flutter_study/prop/HttpProp.dart';

class HttpRequests {
  static Dio dio;

  static void init() {
    dio = Dio();
  }

  static Future<HttpResult> get(String path, Map<String, dynamic> parameters,
      Map<String, String> headers) async {
    return _doRequest(path, parameters, null, headers, "get", null);
  }

  static Future<HttpResult> post(String path, Map<String, dynamic> parameters,
      Map<String, String> headers) async {
    return _doRequest(path, parameters, null, headers, "post", null);
  }

  static Future<HttpResult> postRaw(
      String path, dynamic data, Map<String, String> headers) async {
    return _doRequest(path, null, data, headers, "post", null);
  }

  static Future<HttpResult> getWith(
      String path,
      Map<String, dynamic> parameters,
      Map<String, String> headers,
      int timeoutMilliseconds) async {
    return _doRequest(
        path, parameters, null, headers, "get", timeoutMilliseconds);
  }

  static Future<HttpResult> postWith(
      String path,
      Map<String, dynamic> parameters,
      Map<String, String> headers,
      int timeoutMilliseconds) async {
    return _doRequest(
        path, parameters, null, headers, "post", timeoutMilliseconds);
  }

  static Future<HttpResult> postRawWith(String path, dynamic data,
      Map<String, String> headers, int timeoutMilliseconds) async {
    return _doRequest(path, null, data, headers, "post", timeoutMilliseconds);
  }

  static Future<HttpResult> _doRequest(
      String path,
      Map<String, dynamic> parameters,
      dynamic data,
      Map<String, String> headers,
      String method,
      int timeoutMilliseconds) async {
    HttpResult result = HttpResult();
    try {
      dio.options =
          _buildOption(parameters, data, headers, timeoutMilliseconds);
      Response response;
      if ("get".compareTo(method) == 0) {
        response = await dio.get(path, queryParameters: parameters);
      } else {
        response = await dio.post(path, queryParameters: parameters, data: data);
      }
      result.responseBody = response.data;
      result.statusCode = response.statusCode;
      result.headers = response.headers?.map;
      return result;
    } catch (e) {
      Logs.info('error' + e.toString());
    }
    return result;
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
}
