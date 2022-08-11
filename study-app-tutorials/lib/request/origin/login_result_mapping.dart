import 'dart:convert';

import 'package:tutorials/component/http/http_result.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/request/model/login/login_request_result.dart';
import 'package:tutorials/request/origin/login_result.dart';

class LoginResultMapping {

  static LoginRequestResult mapping(HttpResult httpResult) {
    Logs.info('httpResult ${httpResult.responseBody}');

    LoginResult loginResult = LoginResult.fromJson(jsonDecode(httpResult.responseBody));


    LoginRequestResult result = LoginRequestResult();

    return result;
  }

}
