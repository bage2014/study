import 'dart:convert';

import 'package:tutorials/component/http/http_result.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/request/origin/auth_origin_result.dart';

class AuthOriginResultMapping {

  static AuthOriginResult mapping(HttpResult httpResult) {
    Logs.info('httpResult ${httpResult.responseBody}');

    AuthOriginResult originResult = AuthOriginResult.fromJson(jsonDecode(httpResult.responseBody));
    return originResult;

  }

}
