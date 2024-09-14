import 'dart:collection';

import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/request/model/register/register_request_param.dart';
import 'package:tutorials/request/model/register/register_request_result.dart';

class RegisterRequests {
  static Future<RegisterRequestResult> register(
      RegisterRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mock());
    }

    Map<String, String> param = HashMap();
    param.putIfAbsent("username", () => requestParam?.userName ?? '');
    param.putIfAbsent("password", () => requestParam?.password ?? '');

    Map<String, String> header = HashMap();



    return Future.delayed(const Duration(seconds: 1), () => mock());
  }

  static Future<RegisterRequestResult> validSecurityCode(
      RegisterRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = HashMap();
    // param.putIfAbsent("param", () => userName);
    // return HttpRequests.post(HttpConstant.url_login, param, null);
    return Future.delayed(const Duration(seconds: 1), () => mock());
  }

  static RegisterRequestResult mock() {
    RegisterRequestResult result = RegisterRequestResult();
    result.common.code = 200;
    // result.common.message = "404啦啦啦";
    Logs.info('request result : ${result?.toString()}');
    return result;
  }
}
