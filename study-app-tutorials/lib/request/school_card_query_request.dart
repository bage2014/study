import 'dart:collection';
import 'dart:convert';

import 'package:tutorials/component/cache/setting_caches.dart';
import 'package:tutorials/component/http/http_requests.dart';
import 'package:tutorials/component/log/logs.dart';
import 'package:tutorials/constant/http_constant.dart';
import 'package:tutorials/request/model/feedback/message_feedback.dart';
import 'package:tutorials/request/model/feedback/message_feedback_delete_request_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback_delete_request_result.dart';
import 'package:tutorials/request/model/feedback/message_feedback_insert_request_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback_insert_request_result.dart';
import 'package:tutorials/request/model/feedback/message_feedback_query_request_param.dart';
import 'package:tutorials/request/model/feedback/message_feedback_query_request_result.dart';
import 'package:tutorials/request/model/school/school_card_query_request_param.dart';
import 'package:tutorials/request/model/tv/tv_query_request_param.dart';
import 'package:tutorials/request/model/tv/tv_query_request_result.dart';
import 'package:tutorials/request/origin/school_card_query_result.dart';
import 'package:tutorials/utils/app_utils.dart';

class SchoolCardRequests {


  static Future<SchoolCardQueryResult> query(
      SchoolCardQueryRequestParam requestParam) async {
    if(await SettingCaches.getMockSwitch() == 'true'){
      return Future.delayed(const Duration(seconds: 1), () => mock());
    }

    Logs.info('request param : ${requestParam?.toString()}');
    Map<String, String> param = new HashMap();
    param.putIfAbsent("param", () => json.encode(requestParam));

    return Future.value(
        HttpRequests.get(HttpConstant.url_tv_query_page, param, null).then((value) {
          Logs.info('request result : ${value.responseBody}');
          return SchoolCardQueryResult.fromJson(jsonDecode(value.responseBody));
        }));
  }

  static SchoolCardQueryResult mock() {
    String mock = '{"code":200,"originCode":0,"msg":null,"originMsg":null,"data":[{"id":123,"userId":null,"name":"河海大学","subject":"计算机科学与技术","timeStart":"2013-09-06 00:00:00","timeEnd":"2017-06-24 00:00:00","imageUrl":"https://upload.wikimedia.org/wikipedia/zh/thumb/3/3f/Hohai_University_logo.svg/630px-Hohai_University_logo.svg.png?20220328102013"}],"pagination":{"targetPage":0,"pageSize":10,"total":13}}';
    Logs.info('request result mock : ${mock}');
    return SchoolCardQueryResult.fromJson(jsonDecode(mock));

  }

}
