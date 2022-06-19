

import 'dart:collection';

import 'package:tutorials/component/http/HttpRequests.dart';
import 'package:tutorials/component/log/Logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/request/model/login_request_param.dart';
import 'package:tutorials/request/model/login_request_result.dart';


class LoginRequests {

  static Future<LoginRequestResult> login(LoginRequestParam requestParam) async {
    Logs.info('param : ${requestParam?.toString()}');
    Map<String, String> param = HashMap();
    // param.putIfAbsent("param", () => userName);
    // return HttpRequests.post(HttpConstant.url_login, param, null);
    return Future.delayed(const Duration(seconds: 1),() => mock());
  }

  static LoginRequestResult mock(){
    LoginRequestResult result = LoginRequestResult();
    result.common.code = 200;
    // result.message = "404啦啦啦";
    result.id = 12345;
    result.userName = '小陆[已登陆]';
    result.mail = 'bage@qq.com';
    result.iconUrl = 'hhh';
    return result;
  }

}
