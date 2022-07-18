import 'package:dio/dio.dart';

class CancelRequests {

  CancelToken token = CancelToken();

  void cancel([dynamic reason]) {
    token.cancel(reason);
  }

}
