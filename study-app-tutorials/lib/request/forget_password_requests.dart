import 'dart:collection';

import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/request/model/forget/forget_password_request_param.dart';
import 'package:tutorials/request/model/forget/forget_password_request_result.dart';

class ForgetPasswordRequests {
  static Future<ForgetPasswordRequestResult> reset(
      ForgetPasswordRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = HashMap();
    // param.putIfAbsent("param", () => userName);
    // return HttpRequests.post(HttpConstant.url_login, param, null);
    return Future.delayed(const Duration(seconds: 1), () => mock());
  }

  static ForgetPasswordRequestResult mock() {
    ForgetPasswordRequestResult result = ForgetPasswordRequestResult();
    result.common.code = 200;
    // result.common.message = "404啦啦啦";
    Logs.info('request result : ${result?.toString()}');
    return result;
  }
}
