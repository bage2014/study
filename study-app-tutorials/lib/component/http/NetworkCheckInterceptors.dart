import 'package:tutorials/component/toast/Toasts.dart';
import 'package:tutorials/utils/AppUtils.dart';
import 'package:dio/dio.dart';

class NetworkCheckInterceptors implements InterceptorsWrapper {

  NetworkCheckInterceptors(){
  }


  @override
  void onError(DioError err, ErrorInterceptorHandler handler) {
    AppUtils.isConnected().then((value) => {
      if(!value){
        Toasts.show('请先连接网络')
      }
    });
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
