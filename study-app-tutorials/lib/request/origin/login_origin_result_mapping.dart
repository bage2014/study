import 'dart:convert';

import 'package:tutorials/component/http/http_result.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/request/model/login/login_request_result.dart';

class LoginOriginResultMapping {

  static LoginRequestResult mapping(HttpResult httpResult) {
    Logs.info('httpResult ${httpResult.responseBody}');

    LoginRequestResult result = LoginRequestResult();

    return result;
  }

}
