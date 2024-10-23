import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/utils/app_utils.dart';
import 'package:dio/dio.dart';

/**
 * https://stackoverflow.com/questions/56740793/using-interceptor-in-dio-for-flutter-to-refresh-token
 */
class RefreshTokenInterceptors implements InterceptorsWrapper {

  RefreshTokenInterceptors(){
  }


  @override
  void onError(DioError err, ErrorInterceptorHandler handler) {
    // int? statusCode = err.response?.statusCode;
    // if (statusCode == 403 || statusCode == 401) {
    //   await refreshToken();
    //   return _retry(error.request);
    // }
    // return err.response;
  }

  @override
  void onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    handler.next(options); //continue
  }

  @override
  void onResponse(
      Response<dynamic> response, ResponseInterceptorHandler handler) {
    handler.next(response); //continue
  }
}
