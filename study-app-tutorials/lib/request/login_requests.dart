import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/cache/token_caches.dart';
import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/component/http/http_result.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/error_code_constant.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/constant/sp_constant.dart';
import 'package:tutorials/request/model/login/login_request_param.dart';
import 'package:tutorials/request/model/login/login_request_result.dart';
import 'package:tutorials/request/origin/auth_origin_result.dart';
import 'package:tutorials/request/origin/login_origin_result.dart';
import 'package:tutorials/request/origin/login_origin_result_mapping.dart';
import 'package:tutorials/utils/crypt_utils.dart';

class LoginRequests {

  static Future<LoginRequestResult> tryLogin(
      String token) async {
    Logs.info('request param : ${token}');

    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mock());
    }

    Map<String, String> param = HashMap();
    Map<String, String> header = HashMap();
    header.putIfAbsent(
        "Authorization", () => "Bearer ${token}");
    Logs.info('request header : ${header?.toString()}');

    return Future.value(
        HttpRequests.post(HttpConstant.url_user_profile_detail, param, header)
            .then((value) => LoginOriginResultMapping.mapping(value)));

  }

  static Future<AuthOriginResult> _auth(LoginRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = HashMap();
    param.putIfAbsent("grant_type", () => 'password');
    param.putIfAbsent("username", () => requestParam?.userName ?? '');
    param.putIfAbsent("password", () => requestParam?.password ?? '');

    Map<String, String> header = HashMap();
    String userAndPass = "client:secret";
    header.putIfAbsent(
        "Authorization", () => "Basic " + CryptUtils.encode(userAndPass));

    return Future.value(
        HttpRequests.post(HttpConstant.url_login, param, header).then((value) {
          return AuthOriginResult.fromJson(jsonDecode(value.responseBody));
    }));

  }

  static Future<AuthOriginResult> _authClients(LoginRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = HashMap();
    param.putIfAbsent("grant_type", () => 'client_credentials');

    Map<String, String> header = HashMap();
    String userAndPass = "myappclient:myappsecrte";
    header.putIfAbsent(
        "Authorization", () => "Basic " + CryptUtils.encode(userAndPass));

    return Future.value(
        HttpRequests.post(HttpConstant.url_login, param, header).then((value) {
          return AuthOriginResult.fromJson(jsonDecode(value.responseBody));
        }));

  }
  static Future<LoginRequestResult> login(
      LoginRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mock());
    }

    AuthOriginResult auth = await _auth(requestParam);
    Logs.info('request result : ${auth.toJson()}');
    if(auth?.code != 200){
      LoginRequestResult result = LoginRequestResult();
      result.common.code = auth.code??0;
      result.common.message = auth.msg??'';
      return result;
    }

    TokenCaches.cacheAccessToken(auth?.data?.accessToken??'');
    TokenCaches.cacheRefreshToken(auth?.data?.refreshToken??'');

    Map<String, String> param = HashMap();
    Map<String, String> header = HashMap();
    header.putIfAbsent(
        "Authorization", () => "Bearer ${auth?.data?.accessToken ?? ''}");
    Logs.info('request header : ${header?.toString()}');

    return Future.value(
        HttpRequests.post(HttpConstant.url_user_profile_detail, param, header)
            .then((value) => LoginOriginResultMapping.mapping(value)));

  }

  static LoginRequestResult mock() {
    LoginRequestResult result = LoginRequestResult();
    result.common.code = ErrorCodeConstant.success;
    result.common.message = "404啦啦啦";
    result.id = 12345;
    result.userName = '小陆';
    result.mail = 'bage@qq.com';
    result.iconUrl = 'https://avatars.githubusercontent.com/u/18094768?v=4';
    Logs.info('request mock result : ${result?.toString()}');
    return result;
  }

  static Future<LoginRequestResult> visitorLogin(
      LoginRequestParam requestParam) async {
    Logs.info('request param : ${requestParam?.toString()}');
    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mock());
    }

    AuthOriginResult auth = await _authClients(requestParam);
    Logs.info('request result : ${auth.toJson()}');
    if(auth?.code != 200){
      LoginRequestResult result = LoginRequestResult();
      result.common.code = auth.code??0;
      result.common.message = auth.msg??'';
      return result;
    }
    TokenCaches.cacheAccessToken(auth?.data?.accessToken??'');
    TokenCaches.cacheRefreshToken(auth?.data?.refreshToken??'');

    Map<String, String> param = HashMap();
    Map<String, String> header = HashMap();
    header.putIfAbsent(
        "Authorization", () => "Bearer ${auth?.data?.accessToken ?? ''}");
    Logs.info('request header : ${header?.toString()}');

    return Future.value(
        HttpRequests.get(HttpConstant.url_user_profile_me, param, header)
            .then((value) => LoginOriginResultMapping.mapping(value)));

  }
}
