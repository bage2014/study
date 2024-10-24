import 'package:tutorials/component/cache/user_caches.dart';
import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/constant/error_code_constant.dart';
import 'package:tutorials/request/login_requests.dart';
import 'package:dio/dio.dart';
import 'package:tutorials/request/model/login/login_request_param.dart';
import 'package:tutorials/request/model/login/login_request_result.dart';
import 'package:tutorials/request/model/user.dart';

/**
 * https://stackoverflow.com/questions/56740793/using-interceptor-in-dio-for-flutter-to-refresh-token
 */
class RefreshTokenInterceptors implements InterceptorsWrapper {
  RefreshTokenInterceptors() {}

  @override
  void onError(DioError err, ErrorInterceptorHandler handler) async {
    int? statusCode = err.response?.statusCode;

    if (statusCode == 403 || statusCode == 401 ) {
      LoginRequestResult result = await LoginRequests.visitorLogin(LoginRequestParam());
      if (result.common.code == ErrorCodeConstant.success) {
        UserCaches.cacheUser(User.from(result));
        handler.resolve(await HttpRequests.retry(
            err?.requestOptions ?? RequestOptions(path: '')));
      }
    }
    handler.next(err); //continue
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
