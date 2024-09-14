import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/cache/token_caches.dart';
import 'package:tutorials/component/http/cancel_requests.dart';
import 'package:tutorials/component/http/http_byte_result.dart';
import 'package:tutorials/component/http/http_origin_result.dart';
import 'package:tutorials/component/http/http_progress_callback.dart';
import 'package:tutorials/component/http/http_result.dart';
import 'package:tutorials/component/http/network_check_interceptors.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/component/sp/shared_preference_helper.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/constant/sp_constant.dart';
import 'package:tutorials/prop/http_prop.dart';
import 'package:dio/dio.dart';

class HttpRequests {
  static final Dio _dio = Dio();

  static void init() {
    _dio.interceptors.add(NetworkCheckInterceptors());
  }

  static Future<HttpByteResult> upload(
      String path,
      Map<String, dynamic>? parameters,
      List<MultipartFile> files,
      Map<String, String>? headers,
      HttpProgressCallback onProgress,
      CancelRequests cancelRequests) async {
    return _doUploadRequest(
        path, parameters, files, headers, onProgress, cancelRequests);
  }

  static Future<HttpByteResult> getBytes(
      String path,
      Map<String, dynamic>? parameters,
      Map<String, String>? headers,
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

  static Future<HttpResult> get(String path, Map<String, dynamic>? parameters,
      Map<String, String>? headers) async {
    return _doBaseRequest(path, parameters, null, headers, "get", null);
  }

  static Future<HttpResult> post(String path, Map<String, dynamic>? parameters,
      Map<String, String>? headers) async {
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
      Map<String, dynamic>? parameters,
      dynamic data,
      Map<String, String>? headers,
      String method,
      HttpProgressCallback onProgress,
      CancelRequests cancelRequests) async {
    HttpByteResult result = HttpByteResult();
    try {
      path = rebuildUrl(path);
      Logs.info('_doDownloadRequest path = ${path}');
      Logs.info('_doDownloadRequest parameters = ${parameters}');
      Logs.info('_doDownloadRequest data = ${data}');
      _dio.options = _buildDownloadOption(parameters, data, headers);
      Response response;
      if ("get".compareTo(method) == 0) {
        response = await _dio.get(path,
            queryParameters: parameters, cancelToken: cancelRequests.token,
            onReceiveProgress: (int sent, int total) {
          Logs.info("_doDownloadRequest onReceiveProgress ${sent} / ${total}");
          onProgress?.call(sent, total);
        });
      } else {
        response = await _dio.post(path,
            queryParameters: parameters,
            cancelToken: cancelRequests.token,
            data: data, onReceiveProgress: (int sent, int total) {
          Logs.info("_doDownloadRequest onReceiveProgress ${sent} / ${total}");
          onProgress?.call(sent, total);
        });
      }
      Logs.info('_doDownloadRequest statusCode = ${response.statusCode}');
      result.responseBytes = response.data;
      result.statusCode = response.statusCode ?? 500;
      result.headers = response.headers.map;
      return result;
    } catch (e) {
      Logs.info('_doDownloadRequest error' + e.toString());
      result.statusCode = HttpConstant.server_not_response;
    }
    return result;
  }

  static Future<HttpByteResult> _doUploadRequest(
      String path,
      Map<String, dynamic>? parameters,
      List<MultipartFile> files,
      Map<String, String>? headers,
      HttpProgressCallback onProgress,
      CancelRequests cancelRequests) async {
    HttpByteResult result = HttpByteResult();
    try {
      path = rebuildUrl(path);
      Logs.info('_doUploadRequest path = ${path}');
      Logs.info('_doUploadRequest parameters = ${parameters}');
      Logs.info('_doUploadRequest files.length = ${files?.length}');

      Map<String, dynamic> paramMap = parameters ?? {};
      paramMap['files'] = files;
      FormData formData = FormData.fromMap(paramMap);

      _dio.options = BaseOptions(
          baseUrl: HttpProp.getBaseUrl(),
          headers: headers,
          contentType: HttpProp.getContentType(),
          followRedirects: false,
          validateStatus: (status) {
            return (status ?? 500) < 500;
          });
      Response response = await _dio.post(path,
          queryParameters: parameters,
          cancelToken: cancelRequests.token,
          data: formData, onReceiveProgress: (int sent, int total) {
        Logs.info("_doUploadRequest onReceiveProgress ${sent} / ${total}");
        onProgress?.call(sent, total);
      });
      Logs.info('_doUploadRequest statusCode = ${response.statusCode}');
      result.responseBytes = response.data;
      result.statusCode = response.statusCode ?? 500;
      result.headers = response.headers.map;
      return result;
    } catch (e) {
      Logs.info('_doUploadRequest error' + e.toString());
      result.statusCode = HttpConstant.server_not_response;
    }
    return result;
  }

  static Future<HttpResult> _doBaseRequest(
      String path,
      Map<String, dynamic>? parameters,
      dynamic? data,
      Map<String, String>? headers,
      String method,
      int? timeoutMilliseconds) async {
    HttpResult result = HttpResult();
    try {
      path = rebuildUrl(path);
      final token = await SharedPreferenceHelper.get(SpConstant.token_access_key,'');
      Logs.info('_doBaseRequest path = ${path}');
      Logs.info('_doBaseRequest parameters = ${parameters}');
      Logs.info('_doBaseRequest data = ${data}');
      headers ??= new HashMap();
      headers?.putIfAbsent("Authorization", () => "Bearer ${token}");
      Logs.info('_doBaseRequest headers = ${headers}');
      _dio.options = _buildOption(
          parameters ?? {}, data, headers ?? {}, timeoutMilliseconds ?? 6000);
      Response response;
      if ("get".compareTo(method) == 0) {
        response = await _dio.get(path, queryParameters: parameters);
      } else {
        response =
            await _dio.post(path, queryParameters: parameters, data: data);
      }
      Logs.info('_doBaseRequest statusCode = ${response.statusCode}');
      result.responseBody =
          response.data == null || "".compareTo(response.data) == 0
              ? "{}"
              : response.data;
      result.statusCode = response.statusCode ?? 500;
      result.headers = response.headers.map;
      return result;
    } on DioError catch (e) {
      Logs.info('_doBaseRequest DioError ${e.toString()}' );
      Logs.info('_doBaseRequest DioError data ${e.response?.data}');
      result.statusCode = e.response?.statusCode??HttpConstant.server_not_response;
      result.responseBody = '{ "msg":"${e.response?.statusMessage??HttpConstant.server_not_response_msg}" }';

      try{
        if(e.response?.statusCode == 400 && (e?.response?.statusMessage?.isEmpty??true)){
          HttpOriginResult originResult = HttpOriginResult.fromJson(jsonDecode(e.response?.data));
          result.responseBody = '{ "msg":"${originResult.data?.errorDescription??HttpConstant.server_not_response_msg}" }';
        }
      }catch(e){
        Logs.info(e.toString());
      }

    } catch (e) {
      Logs.info('_doBaseRequest error ${e.toString()}' );
      result.statusCode = HttpConstant.server_not_response;
      result.responseBody = '{ "msg":"${HttpConstant.server_not_response_msg}" }';
    }
    return result;
  }

  static BaseOptions _buildDownloadOption(Map<String, dynamic>? parameters,
      dynamic data, Map<String, String>? headers) {
    return BaseOptions(
        baseUrl: HttpProp.getBaseUrl(),
//        receiveTimeout: HttpProp.timeout, // 不需要指定超时时间
        headers: headers,
        contentType: HttpProp.getContentType(),
        responseType: ResponseType.bytes,
        followRedirects: false,
        validateStatus: (status) {
          return (status ?? 500) < 500;
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
