

import 'dart:collection';

import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/request/model/login/login_request_param.dart';
import 'package:tutorials/request/model/login/login_request_result.dart';


class LoginRequests {

  static Future<LoginRequestResult> login(LoginRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = HashMap();
    param.putIfAbsent("param", () => requestParam?.userName??'');
    return Future.value(LoginRequestResultHttpRequests.post(HttpConstant.url_login, param, null).then((value) => value).catchError((){}));
    return LoginRequestResult
    // return Future.delayed(const Duration(seconds: 1),() => mock());
  }

  static LoginRequestResult mock(){
    LoginRequestResult result = LoginRequestResult();
    result.common.code = 200;
    // result.common.code = ErrorCodeConstant.loginSecurityCodeRequired;
    result.common.message = "404啦啦啦";
    result.id = 12345;
    result.userName = '小陆[已登陆]';
    result.mail = 'bage@qq.com';
    result.iconUrl = 'https://avatars.githubusercontent.com/u/18094768?v=4';
    Logs.info('request result : ${result?.toString()}');
    return result;
  }

}
