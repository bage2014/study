import 'package:dio/dio.dart';

class ResponseInterceptors extends InterceptorsWrapper {
  @override
  Future onRequest(RequestOptions options) {
    return super.onRequest(options);
  }

  @override
  Future onError(DioError err) {
    if(DioErrorType.CONNECT_TIMEOUT == err.type){

    }
    return super.onError(err);
  }

  @override
  Future onResponse(Response response) {
    return super.onResponse(response);
  }
}
