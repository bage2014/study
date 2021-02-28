import 'package:dio/dio.dart';
import 'package:flutter_study/component/http/HttpByteResult.dart';
import 'package:flutter_study/component/http/HttpProgressCallback.dart';
import 'package:flutter_study/component/http/HttpResult.dart';
import 'package:flutter_study/component/log/Logs.dart';
import 'package:flutter_study/prop/HttpProp.dart';

class CancelRequests {

  CancelToken token = null;

  CancelRequests() {
    token = CancelToken();
  }

  void cancel([dynamic reason]) {
    token.cancel(reason);
  }

}
