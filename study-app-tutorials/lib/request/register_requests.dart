import 'dart:collection';

import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/error_code_constant.dart';
import 'package:tutorials/request/model/login_request_param.dart';
import 'package:tutorials/request/model/login_request_result.dart';
import 'package:tutorials/request/model/register_request_param.dart';
import 'package:tutorials/request/model/register_request_result.dart';

class RegisterRequests {
  static Future<RegisterRequestResult> register(
      RegisterRequestParam requestParam) async {
    Logs.info('param : ${requestParam?.toString()}');
    Map<String, String> param = HashMap();
    // param.putIfAbsent("param", () => userName);
    // return HttpRequests.post(HttpConstant.url_login, param, null);
    return Future.delayed(const Duration(seconds: 2), () => mock());
  }

  static RegisterRequestResult mock() {
    RegisterRequestResult result = RegisterRequestResult();
    result.common.code = 200;
    // result.common.message = "404啦啦啦";
    return result;
  }
}
