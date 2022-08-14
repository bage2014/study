import 'dart:convert';

import 'package:tutorials/component/http/http_result.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/request/model/login/login_request_result.dart';
import 'package:tutorials/request/origin/login_origin_result.dart';

class LoginOriginResultMapping {

  static LoginRequestResult mapping(HttpResult httpResult) {
    Logs.info('httpResult ${httpResult.responseBody}');

    LoginOriginResult originResult = LoginOriginResult.fromJson(jsonDecode(httpResult.responseBody));

    LoginRequestResult result = LoginRequestResult();
    result.common.code = originResult.code??0;
    result.common.message = originResult.msg??'';

    result.id = originResult.data?.id??0;
    result.userName = originResult.data?.userName??'';
    result.iconUrl = originResult.data?.iconUrl??'';
    result.mail = originResult.data?.mail??'';
    return result;

  }

}
