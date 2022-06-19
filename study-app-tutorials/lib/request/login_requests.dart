

import 'dart:collection';

import 'package:tutorials/component/http/HttpRequests.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/request/model/login_request_param.dart';
import 'package:tutorials/request/model/login_request_result.dart';


class LoginRequests {

  static Future<LoginRequestResult> login(LoginRequestParam loginRequestParam) async {
    Map<String, String> param = HashMap();
    // param.putIfAbsent("param", () => userName);
    // return HttpRequests.post(HttpConstant.url_login, param, null);
    return Future.delayed(const Duration(seconds: 1),() => mock());
  }

  static LoginRequestResult mock(){
    LoginRequestResult result = LoginRequestResult();
    result.userName = '小陆';
    result.sex = '男';
    result.mail = 'bage@qq.com';
    result.url = '小陆';
    return result;
  }

}
